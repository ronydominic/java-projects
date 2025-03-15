package com.madproject.tutortracker;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class staff_home extends AppCompatActivity {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://madproj-a6ad1-default-rtdb.firebaseio.com/");

    Button b_update;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;

    private LinearLayout containerLayout;
    private TextView ed_user,tv_stat,tv_dept;
    EditText ed_room,ed_purpose;
    Switch stat_switch;
    private  String pt="Absent";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);
        tv_stat=(TextView)findViewById(R.id.tv_status);
        tv_dept =(TextView)findViewById(R.id.tv_dept);
        ed_user=(TextView) findViewById(R.id.et_user);
        ed_room=(EditText) findViewById(R.id.et_room);
        ed_purpose=(EditText)findViewById(R.id.et_purpose);
        stat_switch=(Switch)findViewById(R.id.status_switch);
        b_update=(Button) findViewById(R.id.bt_update);
        Intent intent = getIntent();
        String email = intent.getStringExtra("em");
        String password = intent.getStringExtra("pd");
        //String username = intent.getStringExtra("username");
        CharSequence staffBranch = intent.getCharSequenceExtra("branch");
        tv_dept.setText(staffBranch+"");


        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        ImageView imageView = findViewById(R.id.img_menu);
        Switch switchButton = findViewById(R.id.status_switch);
        //
        String nodePath = "/staff/branch/" + staffBranch + "/" + email + "/status";
        String roomnode = "/staff/branch/" + staffBranch + "/" + email + "/room";
        String purposenode = "/staff/branch/" + staffBranch + "/" + email + "/purpose";
        String usernamenode = "/staff/branch/" + staffBranch + "/" + email + "/username";


        databaseReference.child(nodePath).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // Node doesn't exist, create it and set default value
                    databaseReference.child(nodePath).setValue("Absent");
                } else {
                    // Node exists, check if it has a value
                    String status = dataSnapshot.getValue(String.class);
                    if (status == null) {
                        // Node exists but has no value, set default value
                        databaseReference.child(nodePath).setValue("Absent");
                    } else {
                        // Node exists and has a value, perform desired actions
                        // You can access the value of the "status" node using the 'status' variable
                        // Example: System.out.println("Status value: " + status);
                        if(dataSnapshot.getValue().equals("Present"))
                        {
                            switchButton.setChecked(true);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });

        databaseReference.child(roomnode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // Node doesn't exist, create it and set default value
                    databaseReference.child(roomnode).setValue(null);
                } else {
                    // Node exists, check if it has a value
                    String status = dataSnapshot.getValue(String.class);
                    if (status == null) {
                        // Node exists but has no value, set default value
                        databaseReference.child(roomnode).setValue(null);
                    } else {
                        // Node exists and has a value, perform desired actions
                        // You can access the value of the "status" node using the 'status' variable
                        // Example: System.out.println("Status value: " + status);

                        ed_room.setText(dataSnapshot.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
        databaseReference.child(purposenode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // Node doesn't exist, create it and set default value
                    databaseReference.child(purposenode).setValue(null);
                } else {
                    // Node exists, check if it has a value
                    String status = dataSnapshot.getValue(String.class);
                    if (status == null) {
                        // Node exists but has no value, set default value
                        databaseReference.child(purposenode).setValue(null);
                    } else {
                        // Node exists and has a value, perform desired actions
                        // You can access the value of the "status" node using the 'status' variable
                        // Example: System.out.println("Status value: " + status);

                        ed_purpose.setText(dataSnapshot.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
        databaseReference.child(usernamenode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // Node doesn't exist, create it and set default value
                    databaseReference.child(usernamenode).setValue(null);
                } else {
                    // Node exists, check if it has a value
                    String status = dataSnapshot.getValue(String.class);
                    if (status == null) {
                        // Node exists but has no value, set default value
                        databaseReference.child(usernamenode).setValue(null);
                    } else {
                        // Node exists and has a value, perform desired actions
                        // You can access the value of the "status" node using the 'status' variable
                        // Example: System.out.println("Status value: " + status);

                        ed_user.setText(dataSnapshot.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });


        //databaseReference.child("staff").child("branch").child(""+staffBranch).child(""+email).child("status").setValue("Not present");
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle switch button state change
                if (isChecked) {
                    // Switch is ON
                    // Perform some action
                    tv_stat.setText("Present");
                    pt="Present";
                    //databaseReference.child("staff").child("branch").child(""+staffBranch).child(""+email).child("status").setValue("Present");
                } else {
                    // Switch is OFF
                    // Perform some action
                    tv_stat.setText("Absent");
                    pt="Absent";
                    //databaseReference.child("staff").child("branch").child(""+staffBranch).child(""+email).child("status").setValue("Absent");
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    // Handle Home click
                    // Example: startActivity(new Intent(staff_home.this, HomeActivity.class));
                } else if (itemId == R.id.nav_add_schedule) {
                    // Handle Add Schedule click
                    Intent intent = new Intent(staff_home.this, add_schedule.class);
                    intent.putExtra("em", email);
                    //i.putExtra("username", username);
                    intent.putExtra("branch", staffBranch); // Add this line to pass the branch name
                    startActivity(intent);
                } else if (itemId == R.id.nav_view_appointments) {
                    // Handle View Appointments click
                    Intent i = new Intent(staff_home.this, Appointments.class);
                    i.putExtra("sb",staffBranch);
                    i.putExtra("em",email);
                    startActivity(i);
                } else if (itemId == R.id.nav_broadcast_messages) {
                    // Handle Broadcast Messages click
                    Intent i = new Intent(staff_home.this, Broadcast_messages.class);
                    i.putExtra("sb",staffBranch);
                    i.putExtra("em",email);
                    startActivity(i);
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        b_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("staff").child("branch").child(""+staffBranch).child(""+email).child("status").setValue(""+pt);
                databaseReference.child("staff").child("branch").child(""+staffBranch).child(""+email).child("username").setValue(""+ed_user.getText().toString());
                databaseReference.child("staff").child("branch").child(""+staffBranch).child(""+email).child("purpose").setValue(""+ed_purpose.getText().toString());
                databaseReference.child("staff").child("branch").child(""+staffBranch).child(""+email).child("room").setValue(""+ed_room.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data updated successfully
                        Toast.makeText(staff_home.this, "Data updated", Toast.LENGTH_SHORT).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Error occurred while updating data
                                Toast.makeText(staff_home.this, "Data update failed", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

    }

}
