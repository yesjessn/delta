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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.content.pm.ActivityInfo;

import com.box.androidsdk.content.BoxApiFile;
import com.box.androidsdk.content.BoxApiFolder;
import com.box.androidsdk.content.BoxConfig;
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

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class BoxActivity extends ActionBarActivity implements BoxAuthentication.AuthListener
{
    private BoxSession m_session = null;
    private BoxSession m_oldSession = null;

    private ProgressDialog m_dialog;

    private ArrayAdapter<BoxItem> m_adapter;

    private BoxApiFolder m_folderApi;
    private BoxApiFile m_fileApi;


    @Override
    protected void onCreate(Bundle p_savedInstanceState)
    {
        super.onCreate(p_savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_box);

        Intent thisIntent = getIntent();
        Session currSession = (Session) thisIntent.getSerializableExtra("SESSION");

        final Intent startOver = new Intent(BoxActivity.this,SessionPrep.class);

        m_adapter = new BoxItemAdapter(this);

        BoxConfig.IS_LOG_ENABLED = true;
        BoxConfig.CLIENT_ID = Constants.DELTA_BOX_CLIENT_ID;
        BoxConfig.CLIENT_SECRET = Constants.DELTA_BOX_CLIENT_SECRET;
        // needs to match redirect uri in developer settings if set.
        BoxConfig.REDIRECT_URL = Constants.DELTA_BOX_REDIRECT_URL;

        Button uploadBtn = (Button)findViewById(R.id.upload_btn);
        uploadBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                uploadFile();
            }
        });

        Button doneBtn = (Button)findViewById(R.id.done_btn);
        doneBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                startActivity(startOver);
            }
        });

        initialize();
    }

    private void initialize()
    {
        m_adapter.clear();

        m_session = new BoxSession(this, null);
        m_session.setSessionAuthListener(this);
        m_session.authenticate();
    }

    private void showToast(final String p_text)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(BoxActivity.this, p_text, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadRootFolder()
    {
        new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    final BoxListItems folderItems = m_folderApi.getItemsRequest("0").send();
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            //mList.addAll(folderItems);
                            m_adapter.addAll(folderItems);
                        }
                    });
                }
                catch (BoxException e)
                {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    private void uploadFile()
    {
        m_dialog = ProgressDialog.show(BoxActivity.this, getText(R.string.boxsdk_Please_wait), getText(R.string.boxsdk_Please_wait));
        new Thread()
        {
            @Override
            public void run()
            {
                try
                {

                    // upload all files from the internal storage to Box
                    String[] allFiles = fileList();
                    if(allFiles.length > 0) {

                        int numFileUploaded = 0;

                        // upload all files in the app's internal storage
                        for(String uploadFileName :allFiles)
                        {

                            // upload the file
                            InputStream uploadStream = openFileInput(uploadFileName);
                            String destinationFolderId = "0";
                            BoxRequestsFile.UploadFile request = m_fileApi.getUploadRequest(uploadStream, uploadFileName, destinationFolderId);
                            final BoxFile uploadFileInfo = request.send();
                            showToast("Uploaded " + uploadFileInfo.getName());
                            numFileUploaded++;

                            // Upon uploading the file, remove it from device (app's internal storage)
                            File internalStorageDIR = getFilesDir();
                            File file = new File(internalStorageDIR, uploadFileName);
                            if(!file.delete())
                            {
                                showToast("File " + file.getName() + " not deleted");
                            }

                            loadRootFolder();
                        }
                        showToast("Uploaded " + numFileUploaded + " files");
                    }
                    else
                    {
                        showToast("No file to upload");
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    showToast("IOException");
                }
                catch (BoxException e)
                {
                    e.printStackTrace();
                    BoxError error = e.getAsBoxError();
                    if (error != null && error.getStatus() == HttpStatus.SC_CONFLICT)
                    {
                        ArrayList<BoxEntity> conflicts = error.getContextInfo().getConflicts();
                        if (conflicts != null && conflicts.size() == 1 && conflicts.get(0) instanceof BoxFile)
                        {
                            uploadNewVersion((BoxFile) conflicts.get(0));
                            return;
                        }
                    }
                    showToast("Upload failed");
                }
                finally
                {
                    m_dialog.dismiss();
                }
            }
        }.start();
    }

    private void uploadNewVersion(final BoxFile p_file)
    {
        new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    String uploadFileName = Constants.FILE_NAME;
                    InputStream uploadStream = getResources().getAssets().open(uploadFileName);
                    BoxRequestsFile.UploadNewVersion request = m_fileApi.getUploadNewVersionRequest(uploadStream, p_file.getId());
                    final BoxFile uploadFileVersionInfo = request.send();
                    showToast("Uploaded new version of " + uploadFileVersionInfo.getName());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    showToast("IOException");
                }
                catch (BoxException e)
                {
                    e.printStackTrace();
                    showToast("Upload failed");
                }
                finally
                {
                    m_dialog.dismiss();
                }
            }
        }.start();
    }

    private void clearAdapter()
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                m_adapter.clear();
            }
        });
    }

    @Override
    public void onRefreshed(BoxAuthentication.BoxAuthenticationInfo info)
    {
        //necessary for interface - do nothing
    }

    ///////////////// BOX FUNCTIONS ///////////////////////
    @Override
    public void onAuthCreated(BoxAuthentication.BoxAuthenticationInfo p_info)
    {
        m_folderApi = new BoxApiFolder(m_session);
        m_fileApi = new BoxApiFile(m_session);

        loadRootFolder();
    }

    @Override
    public void onAuthFailure(BoxAuthentication.BoxAuthenticationInfo p_info, Exception p_ex)
    {
        if (p_ex != null)
        {
            clearAdapter();
        }
        else if (p_info == null && m_oldSession != null)
        {
            m_session = m_oldSession;
            m_oldSession = null;
            onAuthCreated(m_session.getAuthInfo());
        }
    }

    @Override
    public void onLoggedOut(BoxAuthentication.BoxAuthenticationInfo p_info, Exception p_ex)
    {
        clearAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu p_menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_box, p_menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu p_menu)
    {
        int numAccounts = BoxAuthentication.getInstance().getStoredAuthInfo(this).keySet().size();
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem p_item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = p_item.getItemId();

        if (id == R.id.upload)
        {
            uploadFile();
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(p_item);
        }
    }

    private class BoxItemAdapter extends ArrayAdapter<BoxItem>
    {
        public BoxItemAdapter(Context p_context)
        {
            super(p_context, 0);
        }

        @Override
        public View getView(int p_position, View p_convertView, ViewGroup p_parent)
        {
            BoxItem item = getItem(p_position);
            if (p_convertView == null)
            {
                p_convertView = LayoutInflater.from(getContext()).inflate(R.layout.boxsdk_list_item, p_parent, false);
            }

            TextView name = (TextView) p_convertView.findViewById(R.id.name);
            name.setText(item.getName());

            ImageView icon = (ImageView) p_convertView.findViewById(R.id.icon);
            if (item instanceof BoxFolder)
            {
                icon.setImageResource(R.drawable.boxsdk_icon_folder_yellow_private);
            }
            else
            {
                icon.setImageResource(R.drawable.boxsdk_generic);
            }

            return p_convertView;
        }

    }

}
