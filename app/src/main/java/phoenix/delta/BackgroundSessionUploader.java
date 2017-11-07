package phoenix.delta;

import android.content.Context;
import android.util.Log;

import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.core.GraphErrorCodes;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;

public class BackgroundSessionUploader implements Runnable {
    private static final String TAG = "BGSessUploader";

    private final DeltaOneDriveClient client;
    private final Context context;

    public BackgroundSessionUploader(DeltaOneDriveClient client, Context context) {
        this.client = client;
        this.context = context;
    }

    @Override
    public void run() {
        Log.i(TAG, "BackgroundSessionUploader started");
        try {
            for (File subjectFolder : findLocalSubjectFolders()) {
                for (File sessionFile : findSessionFiles(subjectFolder)) {
                    try {
                        client.uploadSubjectFile(sessionFile, false);
                        Log.i(TAG, "Successfully uploaded " + sessionFile.getName());
                    } catch (ClientException ce) {
                        if (DeltaOneDriveClient.isClientExceptionCode(ce, GraphErrorCodes.NameAlreadyExists)) {
                            Log.i(TAG, "Skipping " + sessionFile.getName() + " already exists");
                            continue;
                        }
                        Log.i(TAG, "Client exception for " + sessionFile.getName() + ": " + ce.toString());
                    } catch (IOException e) {
                        Log.w(TAG, "Error uploading " + sessionFile.getName(), e);
                    }
                }
                File progressFile = findProgressFile(subjectFolder);
                try {
                    boolean uploaded = uploadIfBigger(progressFile);
                    if (uploaded) {
                        Log.i(TAG, "Successfully uploaded " + progressFile.getName());
                    } else {
                        Log.i(TAG, "Skipping file " + progressFile.getName());
                    }
                } catch (IOException e) {
                    Log.w(TAG, "Error uploading " + progressFile.getName(), e);
                }
            }
        } catch (Throwable t) {
            Log.e(TAG, "Uncaught exception", t);
        }
        Log.i(TAG, "BackgroundSessionUploader completed");
    }

    private File[] findLocalSubjectFolders() {
        return context.getFilesDir().listFiles(new FileFilter() {
            @Override
            public boolean accept(File subjectDir) {
                if (!subjectDir.isDirectory()) {
                    return false;
                }
                return findProgressFile(subjectDir).exists();
            }
        });
    }

    private File[] findSessionFiles(File subjectFolder) {
        return subjectFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".csv") && !filename.endsWith("progress.csv");
            }
        });
    }

    private File findProgressFile(File subjectFolder) {
        return new File(subjectFolder, subjectFolder.getName() + "-progress.csv");
    }

    private boolean uploadIfBigger(File file) throws IOException {
        switch (client.isLocalFileBigger(file)) {
            case LocalBigger:
            case MissingRemote:
                client.uploadSubjectFile(file, true);
                return true;
            default:
                return false;
        }
    }
}
