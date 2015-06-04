package phoenix.delta;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.content.pm.ActivityInfo;

import com.box.androidsdk.content.BoxApiFile;
import com.box.androidsdk.content.BoxApiFolder;
import com.box.androidsdk.content.BoxConfig;
import com.box.androidsdk.content.BoxFutureTask;
import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.BoxException;
import com.box.androidsdk.content.auth.BoxAuthentication;
import com.box.androidsdk.content.models.BoxEntity;
import com.box.androidsdk.content.models.BoxError;
import com.box.androidsdk.content.models.BoxFile;
import com.box.androidsdk.content.models.BoxFolder;
import com.box.androidsdk.content.models.BoxItem;
import com.box.androidsdk.content.models.BoxListItems;
import com.box.androidsdk.content.requests.BoxRequestsFile;
import com.box.androidsdk.content.requests.BoxResponse;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BoxActivity extends ActionBarActivity implements BoxAuthentication.AuthListener{

    Button upload_btn, logout_btn, done_btn;
    TextView numFiles;

    // file to upload
    String filename = "testing.txt";
    String displayName = "testing.txt";

    // BOX INFO - DON'T CHANGE THESE
    final String DELTA_BOX_CLIENT_ID = "lb35b8rol4cairzf4rk3uqr5vc6kps34";
    final String DELTA_BOX_CLIENT_SECRET = "qMvy31CNvoZGyfLkTELq2XHilC5DedBn";
    final String DELTA_BOX_REDIRECT_URL = "https://app.box.com/static/sync_redirect.html";

    BoxSession mSession = null;
    BoxSession mOldSession = null;

    private ProgressDialog mDialog;

    private ArrayList<BoxItem> mList;

    private ListView mListView;
    private ArrayAdapter<BoxItem> mAdapter;

    private BoxApiFolder mFolderApi;
    private BoxApiFile mFileApi;

    Intent adminAct;
    Session currSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_box);

        Intent thisIntent = getIntent();
        currSession = (Session) thisIntent.getSerializableExtra("SESSION");

        adminAct = new Intent(BoxActivity.this,AdminActivity.class);
        adminAct.putExtra("SESSION", currSession);

        mList = new ArrayList<>();
        mListView = (ListView) findViewById(android.R.id.list);
        mAdapter = new BoxItemAdapter(this);

        BoxConfig.IS_LOG_ENABLED = true;
        BoxConfig.CLIENT_ID = DELTA_BOX_CLIENT_ID;
        BoxConfig.CLIENT_SECRET = DELTA_BOX_CLIENT_SECRET;
        // needs to match redirect uri in developer settings if set.
        BoxConfig.REDIRECT_URL = DELTA_BOX_REDIRECT_URL;

        upload_btn = (Button)findViewById(R.id.upload_btn);
        upload_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                uploadFile();
            }
        });

        logout_btn = (Button)findViewById(R.id.logout_btn);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mSession.logout();
                initialize();
            }
        });

        done_btn = (Button)findViewById(R.id.done_btn);
        done_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(adminAct);
            }
        });

        initialize();
    }

    private void updateTextView_numFiles () {
        int num = fileList().length;
        numFiles.setText(Integer.toString(num) + " files needs to be uploaded!");
    }

    private void initialize() {
        //mList.clear();
        mAdapter.clear();

        mSession = new BoxSession(this, null);
        mSession.setSessionAuthListener(this);
        mSession.authenticate();
    }

    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BoxActivity.this, text, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadRootFolder() {
        new Thread() {
            @Override
            public void run() {
                try {
                    final BoxListItems folderItems = mFolderApi.getItemsRequest("0").send();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //mList.addAll(folderItems);
                            mAdapter.addAll(folderItems);
                        }
                    });
                } catch (BoxException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    private void uploadFile() {
        mDialog = ProgressDialog.show(BoxActivity.this, getText(R.string.boxsdk_Please_wait), getText(R.string.boxsdk_Please_wait));
        new Thread() {
            @Override
            public void run() {
                try {

                    // upload all files from the internal storage to Box
                    String[] allFiles = fileList();
                    if(allFiles.length > 0) {

                        int numFileUploaded = 0;

                        // upload all files in the app's internal storage
                        for(int i = 0; i < allFiles.length; i++) {

                            // get filename
                            String uploadFileName = allFiles[i];

                            // upload the file
                            // InputStream uploadStream = getResources().getAssets().open(uploadFileName);
                            InputStream uploadStream = openFileInput(uploadFileName);
                            String destinationFolderId = "0";
                            String uploadName = uploadFileName; // use the same file name
                            BoxRequestsFile.UploadFile request = mFileApi.getUploadRequest(uploadStream, uploadName, destinationFolderId);
                            final BoxFile uploadFileInfo = request.send();
                            showToast("Uploaded " + uploadFileInfo.getName());
                            numFileUploaded++;

                            // Upon uploading the file, remove it from device (app's internal storage)
                            File internalStorageDIR = getFilesDir();
                            File file = new File(internalStorageDIR, uploadFileName);
                            file.delete();

                            loadRootFolder();
                        }
                        showToast("Uploaded " + numFileUploaded + " files");
                    }
                    else
                        showToast("No file to upload");

                } catch (IOException e) {
                    e.printStackTrace();
                    showToast("IOException");
                } catch (BoxException e) {
                    e.printStackTrace();
                    BoxError error = e.getAsBoxError();
                    if (error != null && error.getStatus() == HttpStatus.SC_CONFLICT) {
                        ArrayList<BoxEntity> conflicts = error.getContextInfo().getConflicts();
                        if (conflicts != null && conflicts.size() == 1 && conflicts.get(0) instanceof BoxFile) {
                            uploadNewVersion((BoxFile) conflicts.get(0));
                            return;
                        }
                    }
                    showToast("Upload failed");
                } finally {
                    mDialog.dismiss();
                }
                /*
                try {
                    String uploadFileName = filename;
                    InputStream uploadStream = getResources().getAssets().open(uploadFileName);
                    String destinationFolderId = "0";
                    String uploadName = displayName;
                    BoxRequestsFile.UploadFile request = mFileApi.getUploadRequest(uploadStream, uploadName, destinationFolderId);
                    final BoxFile uploadFileInfo = request.send();
                    showToast("Uploaded " + uploadFileInfo.getName());
                    loadRootFolder();
                } catch (IOException e) {
                    e.printStackTrace();
                    showToast("IOException");
                } catch (BoxException e) {
                    e.printStackTrace();
                    BoxError error = e.getAsBoxError();
                    if (error != null && error.getStatus() == HttpStatus.SC_CONFLICT) {
                        ArrayList<BoxEntity> conflicts = error.getContextInfo().getConflicts();
                        if (conflicts != null && conflicts.size() == 1 && conflicts.get(0) instanceof BoxFile) {
                            uploadNewVersion((BoxFile) conflicts.get(0));
                            return;
                        }
                    }
                    showToast("Upload failed");
                } finally {
                    mDialog.dismiss();
                }*/
            }
        }.start();
        //startActivity(adminAct);
    }

    private void uploadNewVersion(final BoxFile file) {
        new Thread() {
            @Override
            public void run() {
                try {
                    String uploadFileName = filename;
                    InputStream uploadStream = getResources().getAssets().open(uploadFileName);
                    BoxRequestsFile.UploadNewVersion request = mFileApi.getUploadNewVersionRequest(uploadStream, file.getId());
                    final BoxFile uploadFileVersionInfo = request.send();
                    showToast("Uploaded new version of " + uploadFileVersionInfo.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                    showToast("IOException");
                } catch (BoxException e) {
                    e.printStackTrace();
                    showToast("Upload failed");
                } finally {
                    mDialog.dismiss();
                }
            }
        }.start();
    }

    private void clearList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mList.clear();
            }
        });
    }

    private void clearAdapter() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.clear();
            }
        });
    }

    ///////////////// BOX FUNCTIONS ///////////////////////
    @Override
    public void onAuthCreated(BoxAuthentication.BoxAuthenticationInfo info) {
        mFolderApi = new BoxApiFolder(mSession);
        mFileApi = new BoxApiFile(mSession);

        loadRootFolder();
        //uploadFile();
    }

    @Override
    public void onRefreshed(BoxAuthentication.BoxAuthenticationInfo info) {
        // do nothing
    }

    @Override
    public void onAuthFailure(BoxAuthentication.BoxAuthenticationInfo info, Exception ex) {
        if (ex != null) {
            //clearList();
            clearAdapter();
        } else if (info == null && mOldSession != null) {
            mSession = mOldSession;
            mOldSession = null;
            onAuthCreated(mSession.getAuthInfo());
        }
    }

    @Override
    public void onLoggedOut(BoxAuthentication.BoxAuthenticationInfo info, Exception ex) {
        //clearList();
        clearAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_box, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        int numAccounts = BoxAuthentication.getInstance().getStoredAuthInfo(this).keySet().size();
        //menu.findItem(R.id.logoutAll).setVisible(numAccounts > 1);
        menu.findItem(R.id.logout).setVisible(numAccounts > 0);
        //menu.findItem(R.id.switch_accounts).setTitle(numAccounts > 0 ? R.string.switch_accounts : R.string.login);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
            //noinspection SimplifiableIfStatement
            if (id == R.id.upload) {
                uploadFile();
                return true;
            } else if (id == R.id.logout) {
                mSession.logout();
                initialize();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // Do Nothing
        ;
    }

    private class BoxItemAdapter extends ArrayAdapter<BoxItem> {
        public BoxItemAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            BoxItem item = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.boxsdk_list_item, parent, false);
            }

            TextView name = (TextView) convertView.findViewById(R.id.name);
            name.setText(item.getName());

            ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
            if (item instanceof BoxFolder) {
                icon.setImageResource(R.drawable.boxsdk_icon_folder_yellow_private);
            } else {
                icon.setImageResource(R.drawable.boxsdk_generic);
            }

            return convertView;
        }

    }

}
