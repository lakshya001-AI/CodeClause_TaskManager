package com.example.taskmanager_codeclause;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddTaskPage extends AppCompatActivity {

    EditText TaskName, Description, Date, Priority, Status;
    Button CreateTaskButton;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TaskInfo taskInfo;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_page);

        TaskName = findViewById(R.id.tasknameedittext);
        Description = findViewById(R.id.descriptionedittext);
        Date = findViewById(R.id.dateedittext);
        Priority = findViewById(R.id.priorityedittext);
        Status = findViewById(R.id.statusedittext);

        CreateTaskButton = findViewById(R.id.createtaskbutton);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("TaskInfo");

        taskInfo = new TaskInfo();

        CreateTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskname = TaskName.getText().toString();
                String description = Description.getText().toString();
                String date = Date.getText().toString();
                String priority = Priority.getText().toString();
                String status = Status.getText().toString();

                if(TextUtils.isEmpty(taskname) && TextUtils.isEmpty(description) && TextUtils.isEmpty(date) && TextUtils.isEmpty(priority) && TextUtils.isEmpty(status)){
                    Toast.makeText(getApplicationContext(), "Please add some data ", Toast.LENGTH_SHORT).show();

                } else {
                    addDatatoFirebase(taskname, description, date, priority, status);
                }


            }
        });
    }





    private void addDatatoFirebase(String taskname , String description, String date, String priority, String status){

      taskInfo.setTaskName(taskname);
      taskInfo.setDescription(description);
      taskInfo.setDate(date);
      taskInfo.setPriority(priority);
      taskInfo.setStatus(status);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.setValue(taskInfo);



                Toast.makeText(getApplicationContext(), "Task Created Successfully", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AddTaskPage.this,MainPage.class );
                startActivity(i);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Sorry ! try again" + error ,Toast.LENGTH_SHORT).show();

                Intent i =  new Intent(AddTaskPage.this, MainPage.class);
                startActivity(i);


            }
        });


    }


}