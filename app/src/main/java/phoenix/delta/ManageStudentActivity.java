package phoenix.delta;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import java.util.*;


public class ManageStudentActivity extends ActionBarActivity {

    Button del_student_btn, add_student_btn, display_add_layout_btn, display_del_layout_btn, done_btn;
    Spinner studentList;
    List<String> list;
    ArrayAdapter<String> dataAdapter;
    String sel_student;
    RelativeLayout add_layout, del_layout;
    EditText getNewStudent;

    Session currSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_manage_student);

        studentList = (Spinner) findViewById(R.id.student_list);
        ////////////////////// drop down list
        list = new ArrayList<>();
        list.add("(Select a student)");
        // add element to student list
        for(int i = 0; i < 10; i++)
            list.add("Student 0" + i);
        for(int i = 10; i < 50; i++)
            list.add("Student " + i);
        Collections.sort(list);
        // add adapter to student list
        dataAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        studentList.setAdapter(dataAdapter);

        ////////////////////// add
        // layout
        add_layout = (RelativeLayout) findViewById(R.id.add_student_layout);
        add_layout.setVisibility(View.INVISIBLE);
        getNewStudent = (EditText)findViewById(R.id.new_student);
        // layout button
        display_add_layout_btn = (Button)findViewById(R.id.btn_add_student);
        display_add_layout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(add_layout.getVisibility() == View.INVISIBLE)
                    add_layout.setVisibility(View.VISIBLE);
                else
                    add_layout.setVisibility(View.INVISIBLE);

            }
        });
        // execution button
        add_student_btn = (Button)findViewById(R.id.btn_add);
        add_student_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String newStudent = getNewStudent.getText().toString();
                if(newStudent.isEmpty())
                    Toast.makeText(ManageStudentActivity.this, "Enter a valid name!", Toast.LENGTH_SHORT).show();
                else if (list.contains(newStudent))
                    Toast.makeText(ManageStudentActivity.this, newStudent + " already exists", Toast.LENGTH_SHORT).show();
                else {
                    list.add(newStudent);
                    dataAdapter.notifyDataSetChanged();
                    Collections.sort(list);
                    Toast.makeText(ManageStudentActivity.this, "Added " + newStudent, Toast.LENGTH_SHORT).show();
                    getNewStudent.setText("");
                    add_layout.setVisibility(View.INVISIBLE);
                }
            }
        });

        ////////////////////// delete
        // layout
        del_layout = (RelativeLayout) findViewById(R.id.del_student_layout);
        del_layout.setVisibility(View.INVISIBLE);
        // layout button
        display_del_layout_btn = (Button)findViewById(R.id.btn_del_student);
        display_del_layout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (del_layout.getVisibility() == View.INVISIBLE)
                    del_layout.setVisibility(View.VISIBLE);
                else
                    del_layout.setVisibility(View.INVISIBLE);

            }
        });
        // execution button
        del_student_btn = (Button)findViewById(R.id.btn_del);
        del_student_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sel_student = String.valueOf(studentList.getSelectedItem());

                if(!sel_student.equals("(Select a student)")){
                    list.remove(sel_student);
                    Toast.makeText(ManageStudentActivity.this, sel_student + " is deleted.", Toast.LENGTH_SHORT).show();
                    studentList.setSelection(0);
                    del_layout.setVisibility(View.INVISIBLE);
                }
                else {
                    Toast.makeText(ManageStudentActivity.this, "Please select a student to delete!", Toast.LENGTH_SHORT).show();
                }

                //studentList.clearChoices();
                //studentList.setChoiceMode(ListView.CHOICE_MODE_NONE);
            }
        });

        done_btn = (Button)findViewById(R.id.done_btn);
        done_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent thisIntent = getIntent();
                currSession = (Session) thisIntent.getSerializableExtra("SESSION");
                Intent adminAct = new Intent(ManageStudentActivity.this,AdminActivity.class);
                adminAct.putExtra("SESSION", currSession);
                startActivity(adminAct);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage_student, menu);
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
