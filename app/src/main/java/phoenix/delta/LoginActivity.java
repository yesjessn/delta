package phoenix.delta;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
//import android.database.sqlite.*;

public class LoginActivity extends ActionBarActivity {

    private String pwFilename = "passwordFile";
    private DUser admin_shared;
    private DUser teacher_shared;

    DatabaseHandler db;

    // Temporarily set password min length to 0 (to accomodate teacher password "" for testing)
    int minLength = 0, maxLength = 12;
    Button teacher_login_btn, admin_login_btn, reg_admin_btn;
    EditText et_username, et_password;
    TextView errMsg;
    String username, password;
    Session newSession;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_login);

        admin_shared = new DUser("superadmin","ucdm1nd", DUser.ADMINISTRATOR);
        teacher_shared = new DUser("teacher","ucdmind", DUser.TEACHER);

        //db = DatabaseHandler.getInstance(this);
        et_username = (EditText)findViewById(R.id.username);
        et_password = (EditText)findViewById(R.id.password);
        errMsg = (TextView) findViewById(R.id.login_error_msg);
        errMsg.setText("");
        //errMsg.setVisibility(View.INVISIBLE);

        teacher_login_btn = (Button)findViewById(R.id.teacher_login_btn);
        teacher_login_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(authentication(DUser.TEACHER)) {
                    newSession = new Session(false);
                    Intent teacherActivity = new Intent(LoginActivity.this,TeacherActivity.class);
                    teacherActivity.putExtra("SESSION", newSession);
                    Toast.makeText(LoginActivity.this, "Logged in as TEACHER", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(LoginActivity.this, TeacherActivity.class));
                    startActivity(teacherActivity);
                }
                else
                    Toast.makeText(LoginActivity.this, "Cannot Log in as TEACHER", Toast.LENGTH_SHORT).show();
            }
        });

        admin_login_btn = (Button)findViewById(R.id.admin_login_btn);
        admin_login_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
/*
                Toast.makeText(LoginActivity.this, ""+db.toString(), Toast.LENGTH_SHORT).show();

                List<DUser> users = db.getAllUsers();
                Toast.makeText(LoginActivity.this, ""+db.getUsersCount(), Toast.LENGTH_SHORT).show();
                String everyone = "";
                for(int i = 0; i < users.size(); i++) {
                    everyone += users.toString() + "\n";
                }
                Toast.makeText(LoginActivity.this, everyone, Toast.LENGTH_LONG).show();
                */

                if(authentication(DUser.ADMINISTRATOR)) {
                    newSession = new Session(true);
                    Intent adminActivity = new Intent(LoginActivity.this,AdminActivity.class);
                    adminActivity.putExtra("SESSION", newSession);
                    Toast.makeText(LoginActivity.this, "Logged in as ADMINISTRATOR", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(LoginActivity.this,AdminActivity.class));
                    startActivity(adminActivity);
                }
                else
                    Toast.makeText(LoginActivity.this, "Cannot Log in as ADMINISTRATOR", Toast.LENGTH_SHORT).show();

            }
        });

        reg_admin_btn = (Button)findViewById(R.id.reg_admin_btn);
        // feature not available now (invisible)
        reg_admin_btn.setVisibility(View.INVISIBLE);
        reg_admin_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Toast.makeText(LoginActivity.this, "Feature Not Available: Registration", Toast.LENGTH_SHORT).show();
                /*
                Intent regAdminAct = new Intent(LoginActivity.this,RegAdminActivity.class);
                Toast.makeText(LoginActivity.this, "I WANT TO JOIN!", Toast.LENGTH_SHORT).show();
                startActivity(regAdminAct);
                */
            }
        });
    }

    private boolean checkAsAdmin (String username, String password) {
        return (username.compareTo(admin_shared.getUsername()) == 0 && admin_shared.checkPassword(password));
    }

    private boolean checkAsTeacher (String username, String password) {
        return (username.compareTo(teacher_shared.getUsername()) == 0 && teacher_shared.checkPassword(password));
    }


    private boolean authentication(int userType) {

        username = et_username.getText().toString();
        password = et_password.getText().toString();

        if(!validInput())
            return false;

        if(userType == DUser.ADMINISTRATOR) {
            return checkAsAdmin(username, password);
        }
        else
            return checkAsTeacher(username, password);

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

    private boolean validLength(String s) {
        return !(s.length() < minLength || s.length() > maxLength);
    }

    private boolean validInput() {

        // check length
        if(!validLength(username) || !validLength(password)) {
            errMsg.setText("Username/ password must have length " + minLength + "-" + maxLength + " characters)");
            return false;
        }

        // check username & password, numeric & alphabetic char only
        if(!isNumAlphaString(username) || !isNumAlphaString(password)) {
            errMsg.setText("Username and/or password must contain numeric and alphabetic characters only");
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

    @Override
    public void onBackPressed () {
        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
    }
}
