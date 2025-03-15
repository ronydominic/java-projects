package com.madproject.tutortracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Appointments extends AppCompatActivity {

    private DatabaseReference appointmentsRef;
    private LinearLayout appointmentsLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);
        Intent in =getIntent();
        String email=in.getStringExtra("em");
        String branch=in.getStringExtra("sb");
        appointmentsRef = FirebaseDatabase.getInstance().getReference().child("staff").child("branch").child(branch).child(email).child("appointments");
        TextView tv_empty=(TextView)findViewById(R.id.tv_empty);
        appointmentsLayout = findViewById(R.id.ll_1);

        // Listen for changes in the appointments data
        appointmentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the appointments layout
                appointmentsLayout.removeAllViews();

                // Iterate through each appointment
                for (DataSnapshot appointmentSnapshot : dataSnapshot.getChildren()) {
                    String appointmentId = appointmentSnapshot.getKey();
                    String purpose = appointmentSnapshot.child("purpose").getValue(String.class);
                    String requestStatus = appointmentSnapshot.child("request_status").getValue(String.class);
                    String username=appointmentSnapshot.child("username").getValue(String.class);
                    String from=appointmentSnapshot.child("from").getValue(String.class);
                    String to =appointmentSnapshot.child("to").getValue(String.class);

                    // Check if request_status node is present and not empty
                    if (appointmentSnapshot.hasChild("request_status") && !requestStatus.isEmpty()) {
                        // Skip adding the item to the layout
                        continue;
                    }

                    // Inflate the appointment item layout
                    View appointmentItem = getLayoutInflater().inflate(R.layout.appointment_item, null);

                    // Set the tag with the appointmentId
                    appointmentItem.setTag(appointmentId);

                    // Set the purpose text
                    TextView fromTextView=appointmentItem.findViewById(R.id.tv_from_time);
                    TextView toTextView=appointmentItem.findViewById(R.id.tv_to_time);
                    TextView purposeTextView = appointmentItem.findViewById(R.id.tv_appo_purpose);
                    TextView userTextView = appointmentItem.findViewById(R.id.tv_appo_username);
                    purposeTextView.setText(purpose);
                    userTextView.setText(username);
                    fromTextView.setText(from);
                    toTextView.setText(to);

                    // Set the text color based on request status
                    if ("approved".equals(requestStatus)) {
                        purposeTextView.setTextColor(Color.GREEN);
                    } else if ("denied".equals(requestStatus)) {
                        purposeTextView.setTextColor(Color.RED);
                    }

                    // Set the click listeners for accept and reject buttons
                    Button acceptButton = appointmentItem.findViewById(R.id.bt_accept);
                    Button rejectButton = appointmentItem.findViewById(R.id.bt_reject);

                    acceptButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Update the request status to "approved" in the database
                            appointmentsRef.child(appointmentId).child("request_status").setValue("approved");
                            appointmentsLayout.removeView(appointmentItem);
                        }
                    });

                    rejectButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Update the request status to "denied" in the database
                            appointmentsRef.child(appointmentId).child("request_status").setValue("denied");
                            appointmentsLayout.removeView(appointmentItem);
                        }
                    });

                    // Add the appointment item to the layout
                    appointmentsLayout.addView(appointmentItem);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error cases if needed
            }
        });
    }
}
