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


public class HomeActivity extends ActionBarActivity
{
    Button m_startAppBtn;

    @Override
    protected void onCreate(Bundle p_savedInstanceState)
    {
        super.onCreate(p_savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_home);

        m_startAppBtn = (Button) findViewById(R.id.start_app_btn);
        m_startAppBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View p_view)
            {
                Toast.makeText(HomeActivity.this, "Starting App", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu p_menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, p_menu);
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
