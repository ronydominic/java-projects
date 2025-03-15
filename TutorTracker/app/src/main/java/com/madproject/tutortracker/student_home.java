package com.madproject.tutortracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class student_home extends AppCompatActivity {

    LinearLayout hl1Layout;
    LinearLayout hl2Layout;
    TextView tvAbsent, tv_room, tv_purpose;

    DatabaseReference branchRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://madproj-a6ad1-default-rtdb.firebaseio.com/" + "/staff/branch");
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://madproj-a6ad1-default-rtdb.firebaseio.com/");
    Spinner spinner1;
    String selectedBranch;
    Spinner spinner2;
    String selectedChild;
    String selectedUsername;
    Button b_stat, b_message, b_sched, b_appo;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
        String email = getIntent().getStringExtra("em");
        String staff_email;

        spinner1 = findViewById(R.id.spinner_1);
        spinner2 = findViewById(R.id.spinner_2);
        b_stat = findViewById(R.id.bt_status);
        b_sched = findViewById(R.id.bt_sched);
        b_message = findViewById(R.id.bt_messages);
        hl1Layout = findViewById(R.id.hl_1);
        hl2Layout = findViewById(R.id.hl_2);
        tvAbsent = findViewById(R.id.tv_absent);
        tv_room = findViewById(R.id.tv_room);
        tv_purpose = findViewById(R.id.tv_purpose);
        b_appo = findViewById(R.id.bt_appo);

        b_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedBranch.equals("select branch") || selectedUsername.equals("Select tutor")) {
                    Toast.makeText(student_home.this, "Select a valid branch and tutor", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(student_home.this, Student_Message_viewer.class);
                    i.putExtra("em_tutor", selectedChild);
                    i.putExtra("sb", selectedBranch);
                    startActivity(i);
                }
            }
        });

        b_appo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedBranch.equals("select branch") || selectedUsername.equals("Select tutor")) {
                    Toast.makeText(student_home.this, "Select a valid branch and tutor", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(student_home.this, BookAppointment.class);
                    i.putExtra("em_tutor", selectedChild);
                    i.putExtra("sb", selectedBranch);
                    i.putExtra("stud_email", email);
                    startActivity(i);
                }
            }
        });

        b_sched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedBranch.equals("select branch") || selectedUsername.equals("Select tutor")) {
                    Toast.makeText(student_home.this, "Select a valid branch and tutor", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(student_home.this, TutorScheduleActivity.class);
                    i.putExtra("em_tutor",selectedChild);
                    i.putExtra("sb",selectedBranch);
                    startActivity(i);
                }
            }
        });

        b_stat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedBranch.equals("select branch") || selectedUsername.equals("Select tutor")) {
                    Toast.makeText(student_home.this, "Select a valid branch and tutor", Toast.LENGTH_SHORT).show();
                } else {
                    String statusnode = "/staff/branch/" + selectedBranch + "/" + selectedChild + "/status";
                    String usernode = "/staff/branch/" + selectedBranch + "/" + selectedChild;

                    dbRef.child(statusnode).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String status = dataSnapshot.getValue(String.class);
                                if (status != null && status.equals("Present")) {
                                    // Node exists and has a value of "Present"
                                    // Perform actions for Present status
                                    inflateView(hl1Layout);
                                    inflateView(hl2Layout);
                                    deflateView(tvAbsent);
                                    dbRef.child(usernode).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String room = snapshot.child("room").getValue(String.class);
                                            String purp = snapshot.child("purpose").getValue(String.class);
                                            if (room != null) {
                                                tv_room.setText(room);
                                            } else {
                                                tv_room.setText("Data Unavailable");
                                            }
                                            if (purp != null) {
                                                tv_purpose.setText(purp);
                                            } else {
                                                tv_purpose.setText("Data Unavailable");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            // Handle onCancelled event if needed
                                        }
                                    });
                                } else if (status != null && status.equals("Absent")) {
                                    // Node exists and has a value of "Absent"
                                    // Perform actions for Absent status
                                    deflateLayout(hl1Layout);
                                    deflateLayout(hl2Layout);
                                    inflateView(tvAbsent);
                                } else {
                                    // Handle other status values if needed
                                }
                            } else {
                                // Node doesn't exist or has no value
                                Toast.makeText(student_home.this, "Data Unavailable", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle onCancelled event if needed
                        }
                    });
                }
            }
        });

        branchRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> branchNames = new ArrayList<>();
                branchNames.add("select branch");

                for (DataSnapshot branchSnapshot : dataSnapshot.getChildren()) {
                    String branchName = branchSnapshot.getKey();
                    branchNames.add(branchName);
                }

                // Create an ArrayAdapter to populate spinner1
                ArrayAdapter<String> adapter1 = new ArrayAdapter<>(student_home.this, android.R.layout.simple_spinner_item, branchNames);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner1.setAdapter(adapter1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error case if necessary
            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBranch = parent.getItemAtPosition(position).toString();
                loadUsernames(selectedBranch);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void inflateView(View view) {
        view.setVisibility(View.VISIBLE);
    }

    private void deflateView(View view) {
        view.setVisibility(View.GONE);
    }

    private void inflateLayout(ViewGroup layout) {
        layout.setVisibility(View.VISIBLE);
    }

    private void deflateLayout(ViewGroup layout) {
        layout.setVisibility(View.GONE);
    }

    private void loadUsernames(String selectedBranch) {
        DatabaseReference selectedBranchRef = branchRef.child(selectedBranch);
        selectedBranchRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> branchChildren = new ArrayList<>();
                List<String> usernames = new ArrayList<>();
                usernames.add("Select tutor");
                branchChildren.add("");

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String branchChild = childSnapshot.getKey();
                    String username = childSnapshot.child("username").getValue(String.class);

                    branchChildren.add(branchChild);
                    usernames.add(username);
                }

                ArrayAdapter<String> adapter2 = new ArrayAdapter<>(student_home.this, android.R.layout.simple_spinner_item, usernames);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter2);

                // Use the branchChildren and usernames lists as needed
                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // Retrieve the position of the selected item
                        int selectedItemPosition = position;

                        // You can use the position to retrieve corresponding values from the arrays
                        selectedChild = branchChildren.get(selectedItemPosition);
                        selectedUsername = usernames.get(selectedItemPosition);

                        // Do something with the selected values
                        // ...
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Handle the case where no item is selected, if necessary
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error case if necessary
            }
        });

    }
}
