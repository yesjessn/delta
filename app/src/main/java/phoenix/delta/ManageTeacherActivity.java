package phoenix.delta;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import java.util.*;


public class ManageTeacherActivity extends ActionBarActivity {

    Button del_teacher_btn, add_teacher_btn, display_add_layout_btn, display_del_layout_btn, done_btn;
    Spinner teacherList;
    List<String> list;
    ArrayAdapter<String> dataAdapter;
    String sel_teacher;
    RelativeLayout add_layout, del_layout;
    EditText getNewTeacher;

    Session currSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_manage_teacher);

        teacherList = (Spinner) findViewById(R.id.teacher_list);
        ////////////////////// drop down list
        list = new ArrayList<>();
        list.add("(Select a teacher)");
        // add element to teacher list
        for(int i = 0; i < 10; i++)
            list.add("Teacher 0" + i);
        for(int i = 10; i < 50; i++)
            list.add("Teacher " + i);
        Collections.sort(list);
        // add adapter to teacher list
        dataAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teacherList.setAdapter(dataAdapter);

        ////////////////////// add
        // layout
        add_layout = (RelativeLayout) findViewById(R.id.add_teacher_layout);
        add_layout.setVisibility(View.INVISIBLE);
        getNewTeacher = (EditText)findViewById(R.id.new_teacher);
        // layout button
        display_add_layout_btn = (Button)findViewById(R.id.btn_add_teacher);
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
        add_teacher_btn = (Button)findViewById(R.id.btn_add);
        add_teacher_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newTeacher = getNewTeacher.getText().toString();
                if (newTeacher.isEmpty())
                    Toast.makeText(ManageTeacherActivity.this, "Enter a valid name!", Toast.LENGTH_SHORT).show();
                else if (list.contains(newTeacher))
                    Toast.makeText(ManageTeacherActivity.this, newTeacher + " already exists", Toast.LENGTH_SHORT).show();
                else {
                    list.add(newTeacher);
                    dataAdapter.notifyDataSetChanged();
                    Collections.sort(list);
                    Toast.makeText(ManageTeacherActivity.this, "Added " + newTeacher, Toast.LENGTH_SHORT).show();
                    getNewTeacher.setText("");
                    add_layout.setVisibility(View.INVISIBLE);
                }
            }
        });

        ////////////////////// delete
        // layout
        del_layout = (RelativeLayout) findViewById(R.id.del_teacher_layout);
        del_layout.setVisibility(View.INVISIBLE);
        // layout button
        display_del_layout_btn = (Button)findViewById(R.id.btn_del_teacher);
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
        del_teacher_btn = (Button)findViewById(R.id.btn_del);
        del_teacher_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sel_teacher = String.valueOf(teacherList.getSelectedItem());

                if(!sel_teacher.equals("(Select a teacher)")){
                    list.remove(sel_teacher);
                    Toast.makeText(ManageTeacherActivity.this, sel_teacher + " is deleted.", Toast.LENGTH_SHORT).show();
                    teacherList.setSelection(0);
                    del_layout.setVisibility(View.INVISIBLE);
                }
                else {
                    Toast.makeText(ManageTeacherActivity.this, "Please select a teacher to delete!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        done_btn = (Button)findViewById(R.id.done_btn);
        done_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent thisIntent = getIntent();
                currSession = (Session) thisIntent.getSerializableExtra("SESSION");
                Intent adminAct = new Intent(ManageTeacherActivity.this,AdminActivity.class);
                adminAct.putExtra("SESSION", currSession);
                startActivity(adminAct);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage_teacher, menu);
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
