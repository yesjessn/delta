package phoenix.delta;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class DoneSessionActivity extends ActionBarActivity {

    Procedure currProcedure;
    EditText comments;

    @Override
    protected void onCreate(Bundle p_savedInstanceState) {
        super.onCreate(p_savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_done_session);

        Intent thisIntent = getIntent();
        currProcedure = (Procedure) thisIntent.getSerializableExtra("PROCEDURE");
        final Session currSession = currProcedure.currentSession;

        TextView prompt = (TextView) findViewById(R.id.prompt);
        prompt.setGravity(Gravity.CENTER);

        comments = (EditText) findViewById(R.id.comments);

        Button doneBtn = (Button) findViewById(R.id.done_btn);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                currSession.comments = comments.getText().toString();
                if(!currProcedure.endSession(getApplicationContext()))
                {
                    Toast.makeText(DoneSessionActivity.this, "Cannot write to file", Toast.LENGTH_LONG).show();
                }
                else {
                    Intent boxAct = new Intent(DoneSessionActivity.this,BoxActivity.class);
                    boxAct.putExtra("PROCEDURE", currProcedure);
                    startActivity(boxAct);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu p_menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_done_session, p_menu);
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
