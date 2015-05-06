package phoenix.delta;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;


public class TrialSettingActivity extends ActionBarActivity {

    Session currSession;
    String[] nt, itp, tip, iinp;
    NumberPicker num_trial_picker, init_time_picker, time_inc_picker, inc_interval_num_picker;
    Button save_change_btn, cancel_btn;
    Intent resultIntent;
    boolean updated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial_setting);

        //////////// get session from admin activity
        Intent thisIntent = getIntent();
        currSession = (Session) thisIntent.getSerializableExtra("SESSION");
        //Toast.makeText(TrialSettingActivity.this, currSession.toString(), Toast.LENGTH_SHORT).show();

        //////////// number of trial picker
        num_trial_picker = (NumberPicker) findViewById(R.id.num_trial_picker);
        nt = generateDisplayValue(5,100,5); // min, max, inc
        num_trial_picker.setMinValue(0);
        num_trial_picker.setMaxValue(nt.length-1);
        num_trial_picker.setDisplayedValues(nt);
        num_trial_picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        //////////// initial trial time picker
        init_time_picker = (NumberPicker) findViewById(R.id.itt_picker);
        itp = generateDisplayValue(5,60,5); // min, max, inc
        init_time_picker.setMinValue(0);
        init_time_picker.setMaxValue(itp.length-1);
        init_time_picker.setDisplayedValues(itp);
        init_time_picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        //////////// time increment picker
        time_inc_picker = (NumberPicker) findViewById(R.id.ii_picker);
        tip = generateDisplayValue(0,20,1); // min, max, inc
        time_inc_picker.setMinValue(0); // index
        time_inc_picker.setMaxValue(tip.length-1); // index
        time_inc_picker.setDisplayedValues(tip);
        time_inc_picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        //////////// time increment interval
        inc_interval_num_picker = (NumberPicker) findViewById(R.id.inc_trial_picker);
        iinp = generateDisplayValue(0,20,1); // min, max, inc
        inc_interval_num_picker.setMinValue(0); // index
        inc_interval_num_picker.setMaxValue(iinp.length-1); // index
        inc_interval_num_picker.setDisplayedValues(iinp);
        inc_interval_num_picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        //////////// save change button
        save_change_btn = (Button)findViewById(R.id.save_change);
        save_change_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                // get selected values
                int numTrial = Integer.parseInt(nt[num_trial_picker.getValue()]);
                int initTrialTime = Integer.parseInt(itp[init_time_picker.getValue()]);
                int timeInc = Integer.parseInt(tip[time_inc_picker.getValue()]);
                int incTrial = Integer.parseInt(iinp[inc_interval_num_picker.getValue()]);

                // save values
                currSession.changeSetting(numTrial,initTrialTime,timeInc,incTrial);

                //Toast.makeText(TrialSettingActivity.this, numTrial+","+initTrialTime+","+timeInc+","+incTrial,Toast.LENGTH_SHORT).show();

                // pass changed session back to admin activity
                resultIntent = new Intent();
                resultIntent.putExtra("UPDATED_SESSION",currSession);
                setResult(Activity.RESULT_OK, resultIntent);

                Toast.makeText(TrialSettingActivity.this, "Saved Changes", Toast.LENGTH_SHORT).show();
                finish();

            }
        });

        //////////// cancel button (don't change anything)
        cancel_btn = (Button)findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //updated = false;
                //finish();
                resultIntent = new Intent();
                resultIntent.putExtra("UPDATED_SESSION",currSession);
                setResult(Activity.RESULT_CANCELED, resultIntent);
                Toast.makeText(TrialSettingActivity.this, "No Change", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public static String[] generateDisplayValue (int min, int max, int inc) {
        int numOfVal = ((max - min) / inc) + 1;
        String[] displayedValues = new String[numOfVal];
        for(int i = 0; i < numOfVal; i++)
            displayedValues[i] = String.valueOf(min + inc*(i));
        return displayedValues;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trial_setting, menu);
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
