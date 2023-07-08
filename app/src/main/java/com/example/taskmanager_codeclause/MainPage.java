package com.example.taskmanager_codeclause;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainPage extends AppCompatActivity {


    DatabaseReference databaseReference;

    FloatingActionButton floatingActionButton;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;


    TextView TaskName, Description, Date, Priority, Status;

    CardView cardView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        floatingActionButton = findViewById(R.id.floatingActionButton);

        recyclerView = findViewById(R.id.recyclerview);
        adapter = new RecyclerViewAdapter();

        TaskName = findViewById(R.id.tasktextview1);
        Description = findViewById(R.id.descriptiontextview1);
        Date = findViewById(R.id.datetextview1);
        Priority = findViewById(R.id.prioritytextview1);
        Status = findViewById(R.id.textView7);

        cardView = findViewById(R.id.cardview1);

        TaskName.setSelected(true);
        Description.setSelected(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("TaskInfo");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String taskname1 = String.valueOf(snapshot.child("taskName").getValue());
                String status1 = String.valueOf(snapshot.child("status").getValue());
                String priority1 = String.valueOf(snapshot.child("priority").getValue());
                String description1 = String.valueOf(snapshot.child("description").getValue());
                String date1 = String.valueOf(snapshot.child("date").getValue());

                TaskName.setText(taskname1);
                Description.setText(description1);
                Date.setText(date1);
                Priority.setText(priority1);
                Status.setText(status1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error ! Data not Retrieved",Toast.LENGTH_SHORT).show();

            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddTaskPage.class);
                cardView.setVisibility(View.VISIBLE);
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logoutmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.item1:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(), "Successfully Logout", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //finish();
                        MainPage.this.finish();
                        //super.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

    }







}