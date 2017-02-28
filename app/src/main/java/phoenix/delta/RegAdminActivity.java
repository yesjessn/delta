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


public class RegAdminActivity extends ActionBarActivity {

    private String pwFilename = "passwordFile";

    //DatabaseHandler db;

    int minLength = 6, maxLength = 12;
    String secretCode = "193deltaproj";
    Button register_btn, cancel_btn;
    EditText et_username, et_password, et_password_repeat, et_secretcode;
    String username, password, password_repeat, secretCodeIn;
    TextView errMsg;
    Session newSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_reg_admin);

        //db = DatabaseHandler.getInstance(this);

        et_username = (EditText)findViewById(R.id.username);
        et_password = (EditText)findViewById(R.id.password);
        et_password_repeat = (EditText)findViewById(R.id.prompt_repeat_password);
        et_secretcode = (EditText)findViewById(R.id.admin_code);

        errMsg = (TextView) findViewById(R.id.regadmin_error_msg);
        errMsg.setText("");

        register_btn = (Button)findViewById(R.id.register_btn);
        register_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(addAdmin()) {
                    Intent adminActivity = new Intent(RegAdminActivity.this,AdminActivity.class);
                    Toast.makeText(RegAdminActivity.this, "Registered & Logged in as ADMINISTRATOR", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(RegAdminActivity.this, AdminActivity.class));
                    startActivity(adminActivity);
                }
                else
                    Toast.makeText(RegAdminActivity.this, "Error Registering", Toast.LENGTH_SHORT).show();

            }
        });

        cancel_btn = (Button)findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(RegAdminActivity.this, "Cancel Registration", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegAdminActivity.this,SessionPrep.class));
            }
        });

    }

    private boolean addAdmin ()
    {
        username = et_username.getText().toString();
        password = et_password.getText().toString();
        password_repeat = et_password_repeat.getText().toString();
        secretCodeIn = et_secretcode.getText().toString();

        return true;

    }


    private boolean validInput() {

        // check length
        if(!validLength(username) || !validLength(password) || !validLength(password_repeat)) {
            errMsg.setText("Invalid length for password (must have length " + minLength + "-" + maxLength + " characters)");
            return false;
        }

        // check if password & password repeat match
        if(password.compareTo(password_repeat) != 0) {
            errMsg.setText("Passwords mismatch");
            return false;
        }

        // check username & password, numeric & alphabetic char only
        if(!isNumAlphaString(username) || !isNumAlphaString(password)) {
            errMsg.setText("Username and/or password must contain numeric and alphabetic characters only");
            return false;
        }

        // check if secret code matches
        if(secretCode.compareTo(secretCodeIn) != 0) {
            errMsg.setText("Incorrect secret code");
            return false;
        }

        return true;
    }

    private boolean isNumeric(char c) {
        String number = "0123456789";
        return ((number.indexOf(c) == -1)? false : true);
    }

    private boolean isAlpha(char c) {
        String alphabets = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNPOQRSTUVWXYZ";
        return ((alphabets.indexOf(c) == -1)? false : true);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reg_admin, menu);
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
