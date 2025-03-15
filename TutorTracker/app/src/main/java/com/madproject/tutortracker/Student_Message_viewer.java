package com.madproject.tutortracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Student_Message_viewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://madproj-a6ad1-default-rtdb.firebaseio.com/");
        super.onCreate(savedInstanceState);
        Intent in= getIntent();
        String email=in.getStringExtra("em_tutor");
        String branch=in.getStringExtra("sb");
        setContentView(R.layout.activity_student_message_viewer);
        TextView tv =(TextView) findViewById(R.id.tv_message);
        databaseReference.child("staff").child("branch").child(branch).child(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.child("message").getValue() != null) {
                    String message = snapshot.child("message").getValue().toString();
                    tv.setText(message);
                } else {
                    //Toast.makeText(Student_Message_viewer.this, "Values unavailable", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event if needed
            }
        });
    }
}
