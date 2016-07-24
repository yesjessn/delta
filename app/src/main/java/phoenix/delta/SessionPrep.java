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
import android.widget.*;
import java.util.*;


public class SessionPrep extends ActionBarActivity {

    /*
    Button select_student_btn, add_student_btn, enter_id_btn, cancel_btn;
    TableLayout add_stud_layout;
    Spinner studList;
    List<String> list;
    ArrayAdapter<String> dataAdapter;
    EditText getNewStud;
    TextView msg;
    String sel_student;*/

    Button start_btn, cancel_btn;
    EditText et_studID;

    Session currSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_session_prep);

        Intent thisIntent = getIntent();
        currSession = (Session) thisIntent.getSerializableExtra("SESSION");

        et_studID = (EditText) findViewById(R.id.studentID);

        start_btn = (Button)findViewById(R.id.start_btn);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String studID = et_studID.getText().toString();
                if(studID.compareTo("") == 0)
                    Toast.makeText(SessionPrep.this, "Invalid Input: Enter a Student ID!", Toast.LENGTH_SHORT).show();
                else {
                    currSession.setStudent(studID);
                    Intent trialMain = new Intent(SessionPrep.this,TrialMain.class);
                    trialMain.putExtra("SESSION", currSession);
                    Toast.makeText(SessionPrep.this, "Starting trial for: " + studID, Toast.LENGTH_LONG).show();
                    startActivity(trialMain);
                }

            }
        });

        cancel_btn = (Button)findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent nextAct = new Intent(SessionPrep.this, LoginActivity.class);
                nextAct.putExtra("SESSION", currSession);
                startActivity(nextAct);

            }
        });

        /************************************************************************
         * ONCE DATABASE IS READY,
         * CONNECT SPINNER (DROP DOWN MENU) TO DATABASE TO SHOW LIST OF STUDENTS
         ************************************************************************
        studList = (Spinner) findViewById(R.id.spinner_students_list);
        addStudentSpinner();
        dataAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        studList.setAdapter(dataAdapter);

        ////////////////////// selection
        // selection button
        studList = (Spinner) findViewById(R.id.spinner_students_list);
        select_student_btn = (Button)findViewById(R.id.btn_sel_stud);
        select_student_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sel_student = String.valueOf(studList.getSelectedItem());
                if(!sel_student.equals("(Select a student)")) {
                    currSession.setStudent(sel_student);
                    Intent trialMain = new Intent(SessionPrep.this,TrialMain.class);
                    trialMain.putExtra("SESSION", currSession);
                    Toast.makeText(SessionPrep.this, "Selected " + sel_student, Toast.LENGTH_SHORT).show();
                    startActivity(trialMain);
                }
                else {
                    Toast.makeText(SessionPrep.this, "Please select a student to start!", Toast.LENGTH_SHORT).show();
                }
        }
        });


        ////////////////////// add
        // layout
        add_stud_layout = (TableLayout)findViewById(R.id.add_student_layout);
        add_stud_layout.setVisibility(View.INVISIBLE);
        getNewStud = (EditText)findViewById(R.id.new_stud_id);
        msg = (TextView)findViewById(R.id.message);
        // layout button
        add_student_btn = (Button)findViewById(R.id.btn_add_stud);
        add_student_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open up the layout for user to add student
                msg.setText("");
                if(add_stud_layout.getVisibility() == View.INVISIBLE)
                    add_stud_layout.setVisibility(View.VISIBLE);
                else
                    add_stud_layout.setVisibility(View.INVISIBLE);
            }
        });
        // execution button
        enter_id_btn = (Button)findViewById(R.id.btn_enter);
        enter_id_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newID = getNewStud.getText().toString();

                if(newID.isEmpty())
                    msg.setText("Enter a valid id!");
                else if (list.contains(newID))
                    msg.setText(newID + " already exists");
                else {
                    msg.setText("Added " + newID);
                    list.add(newID);
                    Collections.sort(list);
                    Toast.makeText(SessionPrep.this, "List updated", Toast.LENGTH_SHORT).show();
                }

                msg.setVisibility(View.VISIBLE);
                getNewStud.setText("");
                add_stud_layout.setVisibility(View.INVISIBLE);
            }
        });
        */

    }

    /*void addStudentSpinner() {
        list = new ArrayList<>();
        list.add("(Select a student)");
        // add students from database to list
        list.add("Stephanie");
        list.add("Viet");
        list.add("AJ");
        list.add("Tiffany");
        // sort the list
        Collections.sort(list);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_session_prep, menu);
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
