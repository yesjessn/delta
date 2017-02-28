package phoenix.delta;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.microsoft.graph.authentication.MSAAuthAndroidAdapter;
import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.core.DefaultClientConfig;
import com.microsoft.graph.core.GraphErrorCodes;
import com.microsoft.graph.core.IClientConfig;
import com.microsoft.graph.extensions.GraphServiceClient;
import com.microsoft.graph.extensions.IGraphServiceClient;
import com.microsoft.graph.logger.LoggerLevel;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

public class DeltaOneDriveClient {
    public final MSAAuthAndroidAdapter authenticationAdapter;
    public final IGraphServiceClient oneDriveClient;

    public DeltaOneDriveClient (final Activity activity) {
        authenticationAdapter = new MSAAuthAndroidAdapter(activity.getApplication()) {
            @Override
            public String getClientId() {
                return "c84db6c6-611a-4af9-961e-7d27204c632e";
            }

            @Override
            public String[] getScopes() {
                return new String[]{"Files.ReadWrite.AppFolder", "offline_access"};
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
        // https://graph.microsoft.com/v1.0/me/drive/root:/Apps/DeLTA:/children
        final String itemId = "01KLLSCEBPTHTBF23OORB3ISCF7J4WDI43";

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
                    .getRoot().getItemWithPath("/Apps/DeLTA/" + subjectID + "/" + subjectID + "-progress.csv")
                    .getContent()
                    .buildRequest()
                    .get();
        } catch (Exception e) {
            if (e instanceof ClientException) {
                ClientException ce = (ClientException) e;
                if (ce.isError(GraphErrorCodes.ItemNotFound)) {
                    // Don't write error to log, needs log in
                    return true;
                }
            }
            Log.e("ODC", "Error getting progress csv file", e);
            return false;
        }
        Log.i("ODC", "Password csv download successful");

        try {
            FileOutputStream outputStream = context.openFileOutput(subjectID + File.pathSeparator + subjectID + "-progress.csv", Context.MODE_PRIVATE);
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
}