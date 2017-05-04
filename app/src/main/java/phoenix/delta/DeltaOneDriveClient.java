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
import com.microsoft.graph.extensions.IGraphServiceClient;
import com.microsoft.graph.http.GraphErrorResponse;
import com.microsoft.graph.http.GraphServiceException;
import com.microsoft.graph.logger.LoggerLevel;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

public class DeltaOneDriveClient {
    public static DeltaOneDriveClient INSTANCE;

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
        if (!subjectFile.exists())
        {
            subjectFile.mkdir();
        }

        final InputStream inputStream;

        try {
            // This file's details can be found here via the explorer (https://graph.microsoft.io/en-us/graph-explorer)
            // https://graph.microsoft.com/v1.0/me/drive/
            inputStream = oneDriveClient
                    .getMe()
                    .getDrive()
                    .getSpecial("approot")
                    .getItemWithPath(subjectID + "/" + subjectID + "-progress.csv")
                    .getContent()
                    .buildRequest()
                    .get();
        } catch (GraphServiceException clientException) {
            if (clientException.isError(GraphErrorCodes.ItemNotFound)) {
                // Don't write error to log, needs log in
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
                GraphErrorResponse errResponse = (GraphErrorResponse) f.get(clientException);
                if (errResponse.rawObject.getAsJsonObject("error").get("code").getAsString().equalsIgnoreCase(GraphErrorCodes.ItemNotFound.toString())) {
                    return true;
                }
            } catch (Exception ignored) { Log.e("ODC", "error with hacking the response code: ", ignored);}
            Log.e("ODC", "Error received from graph server: " + clientException.getServiceError().code, clientException);
            return false;
        } catch (Exception e) {
            Log.e("ODC", "Unknown error getting progress csv file", e);
            return false;
        }
        Log.i("ODC", "Password csv download successful");

        try {
            File rootDir=new File(context.getFilesDir(), subjectID);
            FileOutputStream outputStream = new FileOutputStream(new File(rootDir, subjectID + "-progress.csv"));
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

    public boolean UploadSubjectData (Context context, String subjectID, String sessionID) {
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
                return false;
            }
        }

        try {
            final File progressFile = new File(subjectFolder, subjectID + "-progress.csv");
            final AtomicReference<ClientException> uploadFailure = new AtomicReference<>();
            uploadSubjectFile(progressFile);
            ClientException ce = uploadFailure.get();
            if (ce != null) {
                Log.e("ODC", "Progress file upload failed", ce);
                return false;
            }
        } catch (FileNotFoundException e) {
            Log.e("ODC", "Progress file not found", e);
            return false;
        } catch (IOException e) {
            Log.e("ODC", "Error uploading progress file", e);
            return false;
        }

        try {
            final File sessionFile = new File(subjectFolder, subjectID + "-" + sessionID + ".csv");
            final AtomicReference<ClientException> uploadFailure = new AtomicReference<>();
            uploadSubjectFile(sessionFile);
            ClientException ce = uploadFailure.get();
            if (ce != null) {
                Log.e("ODC", "Session file upload failed", ce);
                return false;
            }
        } catch (FileNotFoundException e) {
            Log.e("ODC", "Session file not found", e);
            return false;
        } catch (IOException e) {
            Log.e("ODC", "Error uploading session file", e);
            return false;
        }

        return true;
    }

    private void uploadSubjectFile(File file) throws IOException {
        Log.i("ODC", "Starting upload of " + file.getAbsolutePath());
        String subjectID = file.getParentFile().getName();
        String name = file.getName();
        byte[] contents = IOUtils.toByteArray(new FileInputStream(file));

        oneDriveClient
                .getMe()
                .getDrive()
                .getSpecial("approot")
                .getItemWithPath(subjectID + "/" + name)
                .getContent()
                .buildRequest()
                .put(contents);
    }
}