package phoenix.delta;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AdminActivity extends ActionBarActivity {


    TextView trialSetting;
    Button manage_teacher_btn, manage_students_btn, trial_setting_btn,
            start_session_btn, upload_data_btn, logout_btn;
    Session currSession;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_admin);

        Intent thisIntent = getIntent();
        currSession = (Session) thisIntent.getSerializableExtra("SESSION");

        manage_teacher_btn = (Button)findViewById(R.id.manage_teachers);
        // make visible after database works
        manage_teacher_btn.setVisibility(View.INVISIBLE);
        manage_teacher_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent manTeachAct = new Intent(AdminActivity.this, ManageTeacherActivity.class);
                manTeachAct.putExtra("SESSION", currSession);
                Toast.makeText(AdminActivity.this, "Add/delete teachers", Toast.LENGTH_SHORT).show();
                startActivity(manTeachAct);
            }
        });

        manage_students_btn = (Button)findViewById(R.id.manage_students);
        // make visible after database works
        manage_students_btn.setVisibility(View.INVISIBLE);
        manage_students_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent manStudAct = new Intent(AdminActivity.this,ManageStudentActivity.class);
                manStudAct.putExtra("SESSION", currSession);
                Toast.makeText(AdminActivity.this, "Add/delete/edit students", Toast.LENGTH_SHORT).show();
                startActivity(manStudAct);
            }
        });

        trial_setting_btn = (Button)findViewById(R.id.trial_setting);
        trial_setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminActivity.this, "Trial setting", Toast.LENGTH_SHORT).show();
                Intent trialSetting = new Intent(AdminActivity.this,TrialSettingActivity.class);
                trialSetting.putExtra("SESSION", currSession);
                //startActivityForResult(trialSetting, code_setting);
                startActivity(trialSetting);
                updateSetting();
            }
        });

        trialSetting = (TextView)findViewById(R.id.curr_trial_setting);
        updateSetting();

        start_session_btn = (Button)findViewById(R.id.start_session);
        start_session_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startActivity = new Intent(AdminActivity.this,SessionPrep.class);
                startActivity.putExtra("SESSION", currSession);
                Toast.makeText(AdminActivity.this, "Session started", Toast.LENGTH_SHORT).show();
                startActivity(startActivity);
            }
        });

        upload_data_btn = (Button)findViewById(R.id.upload_data);
        //change_password_btn.setVisibility(View.INVISIBLE);
        upload_data_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent boxAct = new Intent(AdminActivity.this,BoxActivity.class);
                boxAct.putExtra("SESSION", currSession);
                startActivity(boxAct);
                //Toast.makeText(AdminActivity.this, "Box is not linked yet", Toast.LENGTH_SHORT).show();
                //Toast.makeText(AdminActivity.this, "Password changed", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(AdminActivity.this,LoginActivity.class));
            }
        });


        logout_btn = (Button)findViewById(R.id.logout);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(AdminActivity.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminActivity.this,LoginActivity.class));
            }
        });

    }

    public void updateSetting() {
        trialSetting.setText(currSession.printableTrialSetting());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin, menu);
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

    @Override
    public void onBackPressed() {
        // Do Nothing
        ;
    }

}
