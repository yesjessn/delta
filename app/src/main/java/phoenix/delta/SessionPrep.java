package phoenix.delta;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import java.util.*;


public class SessionPrep extends ActionBarActivity {

    Button select_student_btn, add_student_btn, enter_id_btn;
    TableLayout add_stud_layout;
    Spinner studList;
    List<String> list;
    ArrayAdapter<String> dataAdapter;
    EditText getNewStud;
    TextView msg;
    String sel_student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_prep);

        studList = (Spinner) findViewById(R.id.spinner_students_list);
        list = new ArrayList<>();
        list.add("(Select a student)");
        list.add("Stephanie");
        list.add("Viet");
        list.add("AJ");
        list.add("Tiffany");
        Collections.sort(list);
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
                    Toast.makeText(SessionPrep.this, "Selected " + sel_student, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SessionPrep.this,TrialMain.class));
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


    }


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
