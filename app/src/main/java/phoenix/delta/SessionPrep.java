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

import java.text.SimpleDateFormat;
import java.util.*;


public class SessionPrep extends ActionBarActivity {

    Button start_btn, cancel_btn;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    EditText et_studID;
    EditText et_RAID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_session_prep);

        Intent thisIntent = getIntent();

        et_studID = (EditText) findViewById(R.id.studentID);
        et_RAID = (EditText) findViewById(R.id.RA);

        start_btn = (Button)findViewById(R.id.start_btn);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                String studID = et_studID.getText().toString();
                String school = spinner.getSelectedItem().toString();
                String RAID = et_RAID.getText().toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                String dateString = dateFormat.format(cal.getTime());

                if(studID.compareTo("") == 0 & RAID.compareTo("") == 0)
                    Toast.makeText(SessionPrep.this, "Invalid Input", Toast.LENGTH_SHORT).show();
                else {
                    Procedure newProcedure = new Procedure(getApplicationContext(), studID, school, RAID, dateString);
                    Intent trialMain = new Intent(SessionPrep.this,SessionStartActivity.class);
                    trialMain.putExtra("PROCEDURE", newProcedure);
                    Toast.makeText(SessionPrep.this, "Starting trial for: " + studID, Toast.LENGTH_LONG).show();
                    startActivity(trialMain);
                }

            }
        });

        spinner = (Spinner)findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(this,R.array.school_names,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), parent.getItemAtPosition(position)+" selected", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cancel_btn = (Button)findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent nextAct = new Intent(SessionPrep.this, LoginActivity.class);
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
