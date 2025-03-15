package com.madproject.tutortracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Broadcast_messages extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://madproj-a6ad1-default-rtdb.firebaseio.com/");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_messages);
        Button bt_clear = findViewById(R.id.bt_clear);
        Button bt_broadcast = findViewById(R.id.bt_broadcast);
        EditText et = findViewById(R.id.ed_broadcast);
        Intent in = getIntent();
        String email = in.getStringExtra("em");
        String branch = in.getStringExtra("sb");

        // Fetch the previously entered data from the database
        DatabaseReference staffRef = databaseReference.child("staff").child("branch").child(branch).child(email);
        staffRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("message")) {
                    String previousMessage = snapshot.child("message").getValue(String.class);
                    et.setText(previousMessage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the database error if needed
            }
        });

        bt_broadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentMessage = et.getText().toString();

                staffRef.child("message").setValue(currentMessage);
            }
        });

        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                staffRef.child("message").setValue("");
                et.setText("");
            }
        });
    }
}
