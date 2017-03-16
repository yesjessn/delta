package phoenix.delta;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class DoneFileUpload extends ActionBarActivity {

    Procedure currProcedure;

    @Override
    protected void onCreate(Bundle p_savedInstanceState) {
        super.onCreate(p_savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_done_file_upload);

        Intent thisIntent = getIntent();
        currProcedure = (Procedure) thisIntent.getSerializableExtra("PROCEDURE");
        final Session currSession = currProcedure.currentSession;

        TextView prompt = (TextView) findViewById(R.id.file_upload_done);

        TextView flagged = (TextView) findViewById(R.id.flagged);
        if (currProcedure.lastSessionAllNow && currSession.allBlockNow())
        {
            flagged.setVisibility(View.VISIBLE);
        } else
        {
            flagged.setVisibility(View.GONE);
        }

        Button doneBtn = (Button) findViewById(R.id.done_btn);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent startOver = new Intent(DoneFileUpload.this, SessionPrep.class);
                startActivity(startOver);
                }
            });

    }
}
