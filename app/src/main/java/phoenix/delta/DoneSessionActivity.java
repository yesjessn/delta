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

    private long m_inPW;
    Procedure currProcedure;
    private EditText m_editTextInPW;
    private ArrayList<Trial> m_allTrialEntries;

    @Override
    protected void onCreate(Bundle p_savedInstanceState)
    {
        super.onCreate(p_savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_done_session);

        Intent thisIntent = getIntent();
        currProcedure = (Procedure) thisIntent.getSerializableExtra("PROCEDURE");
        final Session currSession = currProcedure.currentSession;

        TextView allTrials = (TextView) findViewById(R.id.allTrials);
        TextView prompt = (TextView) findViewById(R.id.prompt);
        prompt.setGravity(Gravity.CENTER);

        m_editTextInPW = (EditText) findViewById(R.id.passphrase);

        m_allTrialEntries = currSession.getAllTrials();
        allTrials.setText(printAllTrials());

        Button doneBtn = (Button) findViewById(R.id.done_btn);
        doneBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {

                if(m_editTextInPW.getText().toString().length() == 0)
                {
                    Toast.makeText(DoneSessionActivity.this, "Enter Passphrase!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    m_inPW = Integer.parseInt(m_editTextInPW.getText().toString());
                    // correct passphrase
                    if(m_inPW == Constants.PASSPHRASE)
                    {

                        // read all data to file ()
                        if(!currProcedure.endSession(getApplicationContext()))
                        {
                            Toast.makeText(DoneSessionActivity.this, "Cannot write to file", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Intent teacherAct = new Intent(DoneSessionActivity.this,SessionPrep.class);
                            teacherAct.putExtra("PROCEDURE", currProcedure);
                            startActivity(teacherAct);
                        }
                    }
                    else
                    {
                        Toast.makeText(DoneSessionActivity.this, "Incorrect Passphrase", Toast
                                .LENGTH_LONG).show();
                    }
                }

            }
        });
    }

    public String printAllTrials ()
    {
        String content = "Results for All Trials: (trial time, response time, choice)\n";

        for(Trial t : m_allTrialEntries)
        {
            content += t.toString() + "\n";
        }

        return content;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu p_menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_done_session, p_menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem p_item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = p_item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(p_item);
    }
}
