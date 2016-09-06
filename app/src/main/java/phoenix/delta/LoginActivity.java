package phoenix.delta;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//import android.database.sqlite.*;

public class LoginActivity extends ActionBarActivity {

    private DUser m_adminShared;
    private DUser m_teacherShared;

    //temporarily set password min length to zero for debugging
    private static final int MIN_LENGTH = 0, MAX_LENGTH = 12;
    private EditText m_etUsername, m_etPassword;
    private TextView m_errMsg;
    private String m_username, m_password;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_login);

        m_adminShared = new DUser("", "", UserType.ADMINISTRATOR);
        m_teacherShared = new DUser("", "", UserType.TEACHER);

        m_etUsername = (EditText) findViewById(R.id.username);
        m_etPassword = (EditText) findViewById(R.id.password);
        m_errMsg = (TextView) findViewById(R.id.login_error_msg);
        m_errMsg.setText("");

        Button teacherLoginBtn = (Button) findViewById(R.id.teacher_login_btn);
        teacherLoginBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if(authentication(UserType.TEACHER))
                {
                    Intent teacherActivity = new Intent(LoginActivity.this, SessionPrep.class);
                    Toast.makeText(LoginActivity.this, "Logged in as TEACHER",
                                   Toast.LENGTH_SHORT).show();
                    startActivity(teacherActivity);
                }
                else Toast.makeText(LoginActivity.this, "Cannot Log in as TEACHER",
                                    Toast.LENGTH_SHORT).show();
            }
        });

        Button adminLoginBtn = (Button)findViewById(R.id.admin_login_btn);
        adminLoginBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if(authentication(UserType.ADMINISTRATOR))
                {
                    Intent adminActivity = new Intent(LoginActivity.this, AdminActivity.class);
                    Toast.makeText(LoginActivity.this, "Logged in as ADMINISTRATOR",
                                   Toast.LENGTH_SHORT).show();
                    startActivity(adminActivity);
                }
                else Toast.makeText(LoginActivity.this, "Cannot Log in as ADMINISTRATOR",
                                    Toast.LENGTH_SHORT).show();

            }
        });

        Button regAdminBtn = (Button)findViewById(R.id.reg_admin_btn);
        // feature not available now (invisible)
        regAdminBtn.setVisibility(View.INVISIBLE);
        regAdminBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Toast.makeText(LoginActivity.this, "Feature Not Available: Registration",
                               Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkAsAdmin (String username, String password) {
        return (username.compareTo(m_adminShared.getUsername()) == 0 && m_adminShared.checkPassword(password));
    }

    private boolean checkAsTeacher (String username, String password) {
        return (username.compareTo(m_teacherShared.getUsername()) == 0 && m_teacherShared.checkPassword(password));
    }


    private boolean authentication(UserType userType) {

        m_username = m_etUsername.getText().toString();
        m_password = m_etPassword.getText().toString();

        if(!validInput())
            return false;

        if(userType == UserType.ADMINISTRATOR) {
            return checkAsAdmin(m_username, m_password);
        }
        else
            return checkAsTeacher(m_username, m_password);
    }

    private boolean isNumeric(char c) {
        String number = "0123456789";
        return (!(number.indexOf(c) == -1));
    }

    private boolean isAlpha(char c) {
        String alphabets = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNPOQRSTUVWXYZ";
        return (!(alphabets.indexOf(c) == -1));
    }

    private boolean isNumAlphaString(String s) {
        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if(!isNumeric(c) && !isAlpha(c))
                return false;
        }
        return true;
    }

    private boolean validLength(String s)
    {
               return !(s.length() < MIN_LENGTH || s.length() > MAX_LENGTH);
    }

    private boolean validInput() {

        // check length
        if(!validLength(m_username) || !validLength(m_password)) {
            m_errMsg.setText(
                    "Username/ password must have length " + MIN_LENGTH + "-" + MAX_LENGTH + " characters)");
            return false;
        }

        // check username & password, numeric & alphabetic char only
        if(!isNumAlphaString(m_username) || !isNumAlphaString(m_password)) {
            m_errMsg.setText(
                    "Username and/or password must contain numeric and alphabetic characters only");
            return false;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
