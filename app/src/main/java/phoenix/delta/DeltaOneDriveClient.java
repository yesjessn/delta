package phoenix.delta;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.microsoft.graph.authentication.MSAAuthAndroidAdapter;
import com.microsoft.graph.concurrency.ChunkedUploadProvider;
import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.concurrency.IProgressCallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.core.DefaultClientConfig;
import com.microsoft.graph.core.GraphErrorCodes;
import com.microsoft.graph.core.IClientConfig;
import com.microsoft.graph.extensions.DriveItem;
import com.microsoft.graph.extensions.DriveItemUploadableProperties;
import com.microsoft.graph.extensions.Folder;
import com.microsoft.graph.extensions.GraphServiceClient;
import com.microsoft.graph.extensions.IDriveItemStreamRequest;
import com.microsoft.graph.extensions.IDriveItemStreamRequestBuilder;
import com.microsoft.graph.extensions.IGraphServiceClient;
import com.microsoft.graph.http.GraphErrorResponse;
import com.microsoft.graph.http.GraphServiceException;
import com.microsoft.graph.logger.LoggerLevel;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.options.QueryOption;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicReference;

public class DeltaOneDriveClient {
    public static DeltaOneDriveClient INSTANCE;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    public final MSAAuthAndroidAdapter authenticationAdapter;
    public final IGraphServiceClient oneDriveClient;

    public DeltaOneDriveClient (final Activity activity) {
        authenticationAdapter = new MSAAuthAndroidAdapter(activity.getApplication()) {
            @Override
            public String getClientId() {
                return "8bf9f2f4-b36b-40c6-81a8-73621723bec8";
            }

            @Override
            public String[] getScopes() {
                return new String[]{"Files.ReadWrite.All", "offline_access"};
            }
        };
        final IClientConfig oneDriveConfig = DefaultClientConfig.createWithAuthenticationProvider(authenticationAdapter);
        oneDriveConfig.getLogger().setLoggingLevel(LoggerLevel.Debug);
        authenticationAdapter.setLogger(oneDriveConfig.getLogger());
        oneDriveClient = new GraphServiceClient.Builder()
                .fromConfig(oneDriveConfig)
                .buildClient();
        Log.i(getClass().getSimpleName(), "Successfully created oneDriveClient");
    }

    /**
     * Downloads the password db file and loads it into a SQLiteDatabase
     *
     * @return null if there were any errors.
     */
    public SQLiteDatabase DownloadPasswordDB(Context ctx) {
        // Documentation:
        // * https://dev.onedrive.com/misc/appfolder.htm
        // * https://dev.onedrive.com/items/list.htm
        // Using https://graph.microsoft.io/en-us/graph-explorer - use URL:
        // https://graph.microsoft.com/v1.0/me/drive/root:/Apps/DelTA:/children
        final String itemId = "01FWWOHCE2XSYEPBE2GBEJZZSGFMIOD7AZ";

        final String passwordDBName = "passwords.db";
        File dbfile = ctx.getDatabasePath(passwordDBName);

        final InputStream inputStream;
        try {
            // This file's details can be found here via the explorer (https://graph.microsoft.io/en-us/graph-explorer)
            // https://graph.microsoft.com/v1.0/me/drive/items/01KLLSCEBPTHTBF23OORB3ISCF7J4WDI43
            inputStream = oneDriveClient
                    .getMe()
                    .getDrive()
                    .getItems(itemId)
                    .getContent()
                    .buildRequest()
                    .get();
        } catch (Exception e) {
            if (e instanceof ClientException) {
                ClientException ce = (ClientException) e;
                if (ce.isError(GraphErrorCodes.AuthenticationFailure)) {
                    // Don't write error to log, needs log in
                    return null;
                }
            }
            Log.e("ODC", "Error getting password db file", e);
            return null;
        }
        Log.i("ODC", "Password db download successful");

        try {
            if (!dbfile.exists()) {
                // FileOutputStream expects the file to exist to write to, so create it and its parent folders if missing.
                dbfile.getParentFile().mkdirs();
                dbfile.createNewFile();
            }
            FileOutputStream outputStream = new FileOutputStream(dbfile);
            IOUtils.copy(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return null;
        }
        Log.i("ODC", "Wrote database to file");

        SQLiteOpenHelper OpenHelper = new SQLiteOpenHelper(ctx, passwordDBName, null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) { }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
        };

        return OpenHelper.getReadableDatabase();
    }

    void loginSilent(ICallback<Void> callback) {
        try {
            Method m = MSAAuthAndroidAdapter.class.getDeclaredMethod("loginSilent", ICallback.class);
            m.setAccessible(true);
            m.invoke(authenticationAdapter, callback);
        } catch (Exception e) {
            Log.i("ODC", "Could not silent log in", e);
        }
    }

    public boolean DownloadProgress (Context context, String subjectID) {
        File root = context.getFilesDir();
        File subjectFile = new File(root, subjectID);
        if (!subjectFile.exists()) {
            subjectFile.mkdir();
        }

        File subjectDir = new File(context.getFilesDir(), subjectID);
        File progressFile = new File(subjectDir, subjectID + "-progress.csv");

        FileSizeComparison bigger = isLocalFileBigger(progressFile);
        switch (bigger) {
            case LocalBigger:
            case EqualSize:
            case MissingRemote:
                Log.i("ODC", "Skipping progress file download: " + bigger);
                return true;
        }

        final InputStream inputStream;

        try {
            // This file's details can be found here via the explorer (https://graph.microsoft.io/en-us/graph-explorer)
            // https://graph.microsoft.com/v1.0/me/drive/
            inputStream = oneDriveClient
                    .getMe()
                    .getDrive()
                    .getSpecial("approot")
                    .getItemWithPath(subjectID + "/" + progressFile.getName())
                    .getContent()
                    .buildRequest()
                    .get();
        } catch (GraphServiceException clientException) {
            if (isClientExceptionCode(clientException, GraphErrorCodes.ItemNotFound)) {
                return true;
            }
            Log.e("ODC", "Error received from graph server: " + clientException.getServiceError().code, clientException);
            return false;
        } catch (Exception e) {
            Log.e("ODC", "Unknown error getting progress csv file", e);
            return false;
        }
        Log.i("ODC", progressFile.getName() + " download successful");

        try {
            FileOutputStream outputStream = new FileOutputStream(progressFile);
            IOUtils.copy(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return false;
        }
        Log.i("ODC", "Wrote csv to file");
        return true;
    }

    public boolean UploadSubjectData (Context context, String subjectID, String sessionID) throws IOException {
        File subjectFolder = context.getFileStreamPath(subjectID);

        // ----------------------------
        // Currently these fail with 500 errors
        // See https://github.com/microsoftgraph/msgraph-sdk-android/issues/43 for the thread created to resolve this issue
        // ----------------------------

        boolean subjectFolderExists;
        try {
            oneDriveClient.getMe().getDrive()
                    .getSpecial("approot")
                    .getItemWithPath(subjectID)
                    .buildRequest()
                    .get();
            subjectFolderExists = true;
        } catch (ClientException e) {
            Log.d("ODC", "Subject folder not found", e);
            subjectFolderExists = false;
        }

        if (!subjectFolderExists) {
            try {
                DriveItem driveSubjectFolder = new DriveItem();
                driveSubjectFolder.name = subjectID;
                driveSubjectFolder.folder = new Folder();
                oneDriveClient
                        .getMe()
                        .getDrive()
                        .getSpecial("approot")
                        .getChildren()
                        .buildRequest()
                        .post(driveSubjectFolder);
            } catch (ClientException e) {
                Log.e("ODC", "Failed to create subject folder", e);
                throw new IOException("Failed to create subject folder on OneDrive.", e);
            }
        }

        try {
            final File progressFile = new File(subjectFolder, subjectID + "-progress.csv");
            uploadSubjectFile(progressFile, true);
        } catch (FileNotFoundException e) {
            Log.e("ODC", "Progress file not found", e);
            throw new IOException("Progress file not found on device.", e);
        } catch (IOException e) {
            Log.e("ODC", "Error uploading progress file", e);
            throw new IOException("Error uploading progress file.", e);
        }

        try {
            final File sessionFile = new File(subjectFolder, subjectID + "-" + sessionID + ".csv");
            uploadSubjectFile(sessionFile, true);
        } catch (FileNotFoundException e) {
            Log.e("ODC", "Session file not found", e);
            throw new IOException("Session file not found on device.", e);
        } catch (IOException e) {
            Log.e("ODC", "Error uploading session file", e);
            throw new IOException("Error uploading session file.", e);
        }

        return true;
    }

    public void uploadSubjectFile(File file, boolean replace) throws IOException {
        Log.i("ODC", "Starting upload of " + file.getAbsolutePath());
        String subjectID = file.getParentFile().getName();
        String name = file.getName();
        byte[] contents = IOUtils.toByteArray(new FileInputStream(file));

        IDriveItemStreamRequestBuilder builder = oneDriveClient
                .getMe()
                .getDrive()
                .getSpecial("approot")
                .getItemWithPath(subjectID + "/" + name)
                .getContent();
        IDriveItemStreamRequest request;
        if (!replace) {
            request = builder.buildRequest(Arrays.<Option>asList(new QueryOption("@name.conflictBehavior", "fail")));
        } else {
            request = builder.buildRequest();
        }
         request.put(contents);
    }

    public enum FileSizeComparison {
        MissingLocal, LocalBigger, EqualSize, RemoteBigger, MissingRemote
    }
    public FileSizeComparison isLocalFileBigger(File file) {
        if (!file.exists()) {
            return FileSizeComparison.MissingLocal;
        }
        String subjectID = file.getParentFile().getName();
        DriveItem item = null;
        try {
            item = oneDriveClient
                    .getMe()
                    .getDrive()
                    .getSpecial("approot")
                    .getItemWithPath(subjectID + "/" + file.getName())
                    .buildRequest()
                    .get();
        } catch (ClientException ce) {
            if (DeltaOneDriveClient.isClientExceptionCode(ce, GraphErrorCodes.ItemNotFound)) {
                return FileSizeComparison.MissingRemote;
            }
        }
        long remote = item.size;
        long local = file.length();
        if (remote > local) {
            return FileSizeComparison.RemoteBigger;
        } else if (local > remote) {
            return FileSizeComparison.LocalBigger;
        } else {
            return FileSizeComparison.EqualSize;
        }
    }

    public static boolean isClientExceptionCode(ClientException ce, GraphErrorCodes code) {
        if (ce.isError(code)) {
            return true;
        }
        // This block to make up for the cases when the error doesn't deserialize properly.
        // Example JSON object:
            /*
            {
                "error": {
                    "code": "itemNotFound",
                    "message": "The resource could not be found.",
                    "innerError": {
                        "request-id": "fbdad64b-42f2-4773-9524-ed5d65628988",
                        "date": "2017-03-08T04:04:45"
                    }
                }
            }
             */
        try {
            Field f = GraphServiceException.class.getDeclaredField("mError");
            f.setAccessible(true);
            GraphErrorResponse errResponse = (GraphErrorResponse) f.get(ce);
            if (errResponse.rawObject.getAsJsonObject("error").get("code").getAsString().equalsIgnoreCase(code.toString())) {
                return true;
            }
        } catch (Exception ignored) { Log.e("ODC", "error with hacking the response code: ", ignored);}
        return false;
    }
}