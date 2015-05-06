package phoenix.delta;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends ActionBarActivity {

    int minLength = 6, maxLength = 12;
    Button teacher_login_btn, admin_login_btn, reg_admin_btn;
    EditText et_username, et_password;
    TextView errMsg;
    String username, password;

    String[][] userdb =
                   {{"stephanie1932015","Spassword"},
                    {"viet1932015","Vpassword"},
                    {"aj1932015", "Apassword"},
                    {"tiffany1932015","Tpassword"}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        errMsg.setText("");
        errMsg.setVisibility(View.INVISIBLE);

        teacher_login_btn = (Button)findViewById(R.id.teacher_login_btn);
        teacher_login_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Logged in as TEACHER", Toast.LENGTH_SHORT).show();
                authentication();
                startActivity(new Intent(LoginActivity.this,TeacherActivity.class));
            }
        });

        admin_login_btn = (Button)findViewById(R.id.admin_login_btn);
        admin_login_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Logged in as ADMINISTRATOR", Toast.LENGTH_SHORT).show();
                authentication();
                startActivity(new Intent(LoginActivity.this,AdminActivity.class));
            }
        });

        reg_admin_btn = (Button)findViewById(R.id.reg_admin_btn);
        reg_admin_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "I WANT TO JOIN!", Toast.LENGTH_SHORT).show();
                authentication();
                startActivity(new Intent(LoginActivity.this,RegAdminActivity.class));
            }
        });
    }
    private boolean authentication() {
        username = et_username.getText().toString();
        password = et_password.getText().toString();

        if(!validFormat()) {
            errMsg.setVisibility(View.VISIBLE);
            return false;
        }

        // check in database
        boolean validUser = false;
        for(int i = 0; i < userdb.length && !validUser; i++) {
            if(username.compareTo(userdb[i][0]) == 0 && password.compareTo(userdb[i][1]) == 0) {
                validUser = true;
            }
        }
        if(validUser)
            return true;
        else {
            errMsg.setText("Wrong username and/ or password");
            return false;
        }
    }
    private boolean isNumeric(char c) {
        String number = "0123456789";
        if(number.indexOf(c) == -1)
            return false;
        else
            return true;
    }

    private boolean isAlpha(char c) {
        String alphabets = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNPOQRSTUVWXYZ";
        if(alphabets.indexOf(c) == -1)
            return false;
        else
            return true;
    }

    private boolean validFormat() {

        // check length
        if(username.length() < minLength || username.length() > maxLength){
            errMsg.setText("Invalid length for username (must have length " + minLength + "-" + maxLength + " characters)");
        }
        if(password.length() < minLength || password.length() > maxLength) {
            errMsg.setText("Invalid length for password (must have length " + minLength + "-" + maxLength + " characters)");
            return false;
        }

        // check for content
        for(int i = 0; i < username.length(); i++) {
            char c = username.charAt(i);
            if(!isNumeric(c) || !isAlpha(c)) {
                errMsg.setText("Username and/or password must contain numeric and alphabetic characters only");
                return false;
            }
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
