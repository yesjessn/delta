package phoenix.delta;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

    private long passphrase = 2015; // changeable
    private long inPW;
    Session currSession;
    TextView prompt, allTrials;
    EditText editText_inPW;
    Button done_btn;
    ArrayList<Trial> allTrialEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_done_session);

        Intent thisIntent = getIntent();
        currSession = (Session) thisIntent.getSerializableExtra("SESSION");

        allTrials = (TextView) findViewById(R.id.allTrials);
        prompt = (TextView) findViewById(R.id.prompt);
        prompt.setGravity(Gravity.CENTER);

        editText_inPW = (EditText) findViewById(R.id.passphrase);


        allTrialEntries = currSession.getAllTrials();
        allTrials.setText(printAllTrials());

        done_btn = (Button) findViewById(R.id.done_btn);
        done_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if(editText_inPW.getText().toString().length() == 0) {
                    Toast.makeText(DoneSessionActivity.this, "Enter Passphrase!", Toast.LENGTH_LONG).show();
                }
                else {
                    inPW = Integer.parseInt(editText_inPW.getText().toString());
                    // correct passphrase
                    if(inPW == passphrase) {

                        // read all data to file ()
                        if(!currSession.endSession(getApplicationContext())) {
                            Toast.makeText(DoneSessionActivity.this, "Cannot write to file", Toast.LENGTH_LONG).show();
                        }
                        else if(currSession.isStartedByAdmin()) {
                            Intent adminAct = new Intent(DoneSessionActivity.this,AdminActivity.class);
                            adminAct.putExtra("SESSION", currSession);
                            startActivity(adminAct);
                        }
                        else {
                            Intent teacherAct = new Intent(DoneSessionActivity.this,TeacherActivity.class);
                            teacherAct.putExtra("SESSION", currSession);
                            startActivity(teacherAct);
                        }
                    }
                    else
                        Toast.makeText(DoneSessionActivity.this, "Incorrect Passphrase", Toast.LENGTH_LONG).show();
                }

            }
        });



    }

    public String printAllTrials () {
        String content = "Results for All Trials: (trial time, response time, choice)\n";

        for(Trial t : allTrialEntries) {
            content += t.toString() + "\n";
        }


        return content;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_done_session, menu);
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
}
