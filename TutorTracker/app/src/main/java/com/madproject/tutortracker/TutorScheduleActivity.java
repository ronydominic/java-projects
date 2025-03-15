package com.madproject.tutortracker;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TutorScheduleActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;
    private List<ScheduleItem> scheduleList;

    private DatabaseReference databaseReference;

    private String branch;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_schedule);
        TextView tv_empty=(TextView)findViewById(R.id.tv_empty);

        // Retrieve the branch and email values from the previous activity
        branch = getIntent().getStringExtra("sb");
        email = getIntent().getStringExtra("em_tutor");
        System.out.println("branch"+branch);
        System.out.println("Email"+email);

        // Initialize the RecyclerView and its adapter
        recyclerView = findViewById(R.id.recyclerView);
        scheduleList = new ArrayList<>();
        adapter = new ScheduleAdapter(scheduleList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Initialize the Firebase Realtime Database reference
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://madproj-a6ad1-default-rtdb.firebaseio.com/")
                .child("staff")
                .child("branch")
                .child(branch)
                .child(email)
                .child("schedule");

        // Read the schedule data from the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("onDataChange triggered");

                scheduleList.clear();

                for (DataSnapshot scheduleSnapshot : dataSnapshot.getChildren()) {
                    String task = scheduleSnapshot.child("task").getValue(String.class);
                    String date = scheduleSnapshot.child("date").getValue(String.class);
                    String from = scheduleSnapshot.child("from").getValue(String.class);
                    String to = scheduleSnapshot.child("to").getValue(String.class);

                    System.out.println("task: " + task);
                    System.out.println("date: " + date);
                    System.out.println("from: " + from);
                    System.out.println("to: " + to);

                    ScheduleItem scheduleItem = new ScheduleItem(task, date, from, to);
                    scheduleList.add(scheduleItem);
                }

                adapter.notifyDataSetChanged();

                if (scheduleList.isEmpty()) {
                    tv_empty.setText("Schedule list is empty");
                } else {
                    tv_empty.setText("");
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("onCancelled triggered");
                // Handle the error
            }
        });

    }}