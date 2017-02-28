package phoenix.delta;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicReference;


public class SessionPrep extends ActionBarActivity {

    Button start_btn, cancel_btn;
    Button onedrive_btn;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    EditText et_subjectD;
    EditText et_RAID;
    EditText et_RAPassword;
    DeltaOneDriveClient oneDriveCilent;
    final AtomicReference<SQLiteDatabase> passwordDBRef = new AtomicReference<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_session_prep);

        et_subjectD = (EditText) findViewById(R.id.subjectID);
        et_RAID = (EditText) findViewById(R.id.RAID);
        et_RAPassword = (EditText) findViewById(R.id.RAPassword);

        onedrive_btn = (Button) findViewById(R.id.onedrive_btn);
        onedrive_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneDriveCilent.authenticationAdapter.logout(new ICallback<Void>() {
                    @Override
                    public void success(Void aVoid) { Log.i("ODC", "Logout success"); }

                    @Override
                    public void failure(ClientException ex) { Log.e("ODC", "Logout failure", ex); }
                });

                oneDriveCilent.authenticationAdapter.login(SessionPrep.this, new ICallback<Void>() {
                    @Override
                    public void success(Void aVoid) {
                        Log.i("ODC", "Login success");
                        asyncDownloadPasswordDB(oneDriveCilent);
                    }

                    @Override
                    public void failure(ClientException ex) { Log.e("ODC", "Login failure", ex); }
                });
            }
        });
        onedrive_btn.setEnabled(false);

        oneDriveCilent = new DeltaOneDriveClient(SessionPrep.this);
        oneDriveCilent.loginSilent(new ICallback<Void>() {
            @Override
            public void success(Void aVoid) {
                Log.i("ODC", "Silent login success");
                asyncDownloadPasswordDB(oneDriveCilent);
            }

            @Override
            public void failure(ClientException ex) {
                Log.e("ODC", "Silent login failure", ex);
                SessionPrep.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onedrive_btn.setEnabled(true);
                    }
                });
            }
        });

        start_btn = (Button) findViewById(R.id.start_btn);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                String subjectID = et_subjectD.getText().toString().toLowerCase();
                String RAID = et_RAID.getText().toString();
                String RAPassword = et_RAPassword.getText().toString();

                if (subjectID.compareTo("") == 0 | spinner.getSelectedItem() == null) {
                    Toast.makeText(SessionPrep.this, "Invalid Input", Toast.LENGTH_SHORT).show();
                } else {

                    String school = spinner.getSelectedItem().toString();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                    String dateString = dateFormat.format(cal.getTime());
                    SQLiteDatabase passwordDB = passwordDBRef.get();
                    if (passwordDB == null) {
                        Toast.makeText(SessionPrep.this.getApplicationContext(), "Password DB has not been downloaded/imported", Toast.LENGTH_LONG);
                        return;
                    }
                    Cursor cursor = passwordDB.query("passwords", new String[]{"password"}, "username = ?", new String[]{RAID}, null, null, null);
                    if (cursor.getCount() == 0){
                        Toast.makeText(SessionPrep.this, "Username not found", Toast.LENGTH_SHORT).show();
                    } else {
                        cursor.moveToFirst();
                        String passwordInDB = cursor.getString(0);
                        if (RAPassword.equals(passwordInDB)) {
                            asyncDownloadProgress(oneDriveCilent, subjectID, school, RAID, dateString);
                            Toast.makeText(SessionPrep.this, "Loading progress csv, may take some time", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(SessionPrep.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });
        start_btn.setEnabled(false);

        spinner = (Spinner) findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.school_names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setPrompt("Select School");
        spinner.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.contact_spinner_row_nothing_selected, this));


        cancel_btn = (Button) findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SessionPrep.this.recreate();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_session_prep, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void asyncDownloadPasswordDB(final DeltaOneDriveClient oneDriveCilent) {
        AsyncTask<DeltaOneDriveClient, Void, SQLiteDatabase> task = new AsyncTask<DeltaOneDriveClient, Void, SQLiteDatabase>() {
            @Override
            protected SQLiteDatabase doInBackground(DeltaOneDriveClient... params) {
                Log.i("SessionPrep", "Starting password DB download");
                DeltaOneDriveClient client = params[0];
                return client.DownloadPasswordDB(getApplicationContext());
            }

            @Override
            protected void onPostExecute(SQLiteDatabase result) {
                if (result != null) {
                    Log.i("SessionPrep", "Password DB import complete");
                    passwordDBRef.set(result);
                }
                start_btn.setEnabled(true);
                onedrive_btn.setEnabled(true);
            }
        };
        task.execute(oneDriveCilent);
    }

    private void asyncDownloadProgress (final DeltaOneDriveClient oneDriveCilent, final String subjectID, final String school, final String RAID, final String dateString) {
        AsyncTask<DeltaOneDriveClient, Void, Boolean> task = new AsyncTask<DeltaOneDriveClient, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(DeltaOneDriveClient... params) {
                Log.i("SessionPrep", "Starting progress csv download");
                DeltaOneDriveClient client = params[0];
                return client.DownloadProgress(getApplicationContext(), subjectID);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                Log.i("SessionPrep", "Progress csv import complete");
                if(result) {
                    Procedure newProcedure = new Procedure(getApplicationContext(), subjectID, school, RAID, dateString);
                    Intent trialMain = new Intent(SessionPrep.this, SessionStartActivity.class);
                    trialMain.putExtra("PROCEDURE", newProcedure);
                    startActivity(trialMain);
                } else {
                    Toast.makeText(SessionPrep.this, "Progress file download failed, please try again", Toast.LENGTH_LONG).show();
                }
            }
        };
        task.execute(oneDriveCilent);

    }
}
