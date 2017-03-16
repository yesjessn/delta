package phoenix.delta;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class DoneSessionActivity extends ActionBarActivity {

    Procedure currProcedure;
    EditText comments;

    @Override
    protected void onCreate(Bundle p_savedInstanceState) {
        super.onCreate(p_savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_done_session);

        Intent thisIntent = getIntent();
        currProcedure = (Procedure) thisIntent.getSerializableExtra("PROCEDURE");
        final Session currSession = currProcedure.currentSession;

        TextView prompt = (TextView) findViewById(R.id.prompt);

        comments = (EditText) findViewById(R.id.comments);

        Button uploadBtn = (Button) findViewById(R.id.upload_btn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                currSession.comments = comments.getText().toString();
                if(!currProcedure.endSession(getApplicationContext()))
                {
                    Toast.makeText(DoneSessionActivity.this, "Cannot write to file", Toast.LENGTH_LONG).show();
                }
                else {
                    asyncUploadSubjectData(DeltaOneDriveClient.INSTANCE, currProcedure, currSession);
                }
            }
        });
    }

    private void asyncUploadSubjectData (final DeltaOneDriveClient oneDriveClient, final Procedure currProcedure, final Session currSession) {
        AsyncTask<DeltaOneDriveClient, Void, Boolean> task = new AsyncTask<DeltaOneDriveClient, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(DeltaOneDriveClient... params) {
                Log.i("DoneSessionActivity", "Starting subject data upload");
                DeltaOneDriveClient client = params[0];
                return client.UploadSubjectData(getApplicationContext(), currProcedure.subjectID, String.valueOf(currSession.sessionID));
            }

            @Override
            protected void onPostExecute(Boolean result) {
                Log.i("DoneSessionActivity", "Subject data upload complete with result: " + result);
                if(result) {
                    Intent uploadFile = new Intent(DoneSessionActivity.this, DoneFileUpload.class);
                    uploadFile.putExtra("PROCEDURE", currProcedure);
                    startActivity(uploadFile);
                } else {
                    Toast.makeText(DoneSessionActivity.this, "Failed to upload data, please try again", Toast.LENGTH_LONG).show();
                }
            }
        };
        task.execute(oneDriveClient);
    }
}
