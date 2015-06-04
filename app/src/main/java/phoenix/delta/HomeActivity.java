package phoenix.delta;

import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.content.Intent;


public class HomeActivity extends ActionBarActivity {

    Button start_app_btn;

    /*
        <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textAppearance="?android:attr/textAppearanceLarge"
        android:text="@string/app_name"
        android:id="@+id/app_name"
        android:fontFamily="sans-serif"
        android:textSize="100dp"
        android:layout_above="@+id/start_app_btn"
        android:layout_centerHorizontal="true" />
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_home);

        start_app_btn = (Button)findViewById(R.id.start_app_btn);
        start_app_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "Starting App", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
