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

public class AdminActivity extends ActionBarActivity
{
    private TextView m_trialSetting;
    private Session m_currSession;

    @Override
    protected void onCreate(Bundle p_savedInstanceState)
    {
        super.onCreate(p_savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_admin);

        Intent thisIntent = getIntent();
        m_currSession = (Session) thisIntent.getSerializableExtra(Constants.SESSION);

        Button manageTeacherBtn = (Button)findViewById(R.id.manage_teachers);
        // make visible after database works
        manageTeacherBtn.setVisibility(View.INVISIBLE);
        manageTeacherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent manTeachAct = new Intent(AdminActivity.this, ManageTeacherActivity.class);
                manTeachAct.putExtra(Constants.SESSION, m_currSession);
                Toast.makeText(AdminActivity.this, "Add/delete teachers", Toast.LENGTH_SHORT).show();
                startActivity(manTeachAct);
            }
        });

        Button manageStudentsBtn = (Button)findViewById(R.id.manage_students);
        // make visible after database works
        manageStudentsBtn.setVisibility(View.INVISIBLE);
        manageStudentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent manStudAct = new Intent(AdminActivity.this,ManageStudentActivity.class);
                manStudAct.putExtra(Constants.SESSION, m_currSession);
                Toast.makeText(AdminActivity.this, "Add/delete/edit students", Toast.LENGTH_SHORT).show();
                startActivity(manStudAct);
            }
        });

        Button trialSettingBtn = (Button)findViewById(R.id.trial_setting);
        trialSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminActivity.this, "Trial setting", Toast.LENGTH_SHORT).show();
                Intent trialSetting = new Intent(AdminActivity.this,TrialSettingActivity.class);
                trialSetting.putExtra(Constants.SESSION, m_currSession);
                startActivity(trialSetting);
                updateSetting();
            }
        });

        m_trialSetting = (TextView)findViewById(R.id.curr_trial_setting);
        updateSetting();

        Button startSessionBtn = (Button)findViewById(R.id.start_session);
        startSessionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startActivity = new Intent(AdminActivity.this,SessionPrep.class);
                startActivity.putExtra(Constants.SESSION, m_currSession);
                Toast.makeText(AdminActivity.this, "Session started", Toast.LENGTH_SHORT).show();
                startActivity(startActivity);
            }
        });

        Button uploadDataBtn = (Button)findViewById(R.id.upload_data);
        uploadDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent boxAct = new Intent(AdminActivity.this,BoxActivity.class);
                boxAct.putExtra(Constants.SESSION, m_currSession);
                startActivity(boxAct);
            }
        });


        Button logoutBtn = (Button)findViewById(R.id.logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(AdminActivity.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminActivity.this,LoginActivity.class));
            }
        });

    }

    public void updateSetting()
    {
        m_trialSetting.setText(m_currSession.printableTrialSetting());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu p_menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin, p_menu);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(p_item);
    }
}
