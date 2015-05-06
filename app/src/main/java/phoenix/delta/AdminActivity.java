package phoenix.delta;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class AdminActivity extends ActionBarActivity {

    final int code_setting = 1;
    Button manage_teacher_btn, manage_students_btn, trial_setting_btn,
            start_session_btn, change_password_btn, logout_btn;
    Session newSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        newSession = new Session();

        manage_teacher_btn = (Button)findViewById(R.id.manage_teachers);
        manage_teacher_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminActivity.this, "Add/delete teachers", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminActivity.this,ManageTeacherActivity.class));
            }
        });

        manage_students_btn = (Button)findViewById(R.id.manage_students);
        manage_students_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminActivity.this, "Add/delete/edit students", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminActivity.this,ManageStudentActivity.class));
            }
        });

        trial_setting_btn = (Button)findViewById(R.id.trial_setting);
        trial_setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminActivity.this, "Trial setting", Toast.LENGTH_SHORT).show();
                Intent trialSetting = new Intent(AdminActivity.this,TrialSettingActivity.class);
                trialSetting.putExtra("SESSION", newSession);
                startActivityForResult(trialSetting, code_setting);

            }
        });

        start_session_btn = (Button)findViewById(R.id.start_session);
        start_session_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminActivity.this, "Session started", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminActivity.this,SessionPrep.class));
            }
        });

        change_password_btn = (Button)findViewById(R.id.change_password);
        change_password_btn.setVisibility(View.INVISIBLE);
        change_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(AdminActivity.this, newSession.toString(), Toast.LENGTH_SHORT).show();

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
}
