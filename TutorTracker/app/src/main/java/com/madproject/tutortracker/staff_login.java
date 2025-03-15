package com.madproject.tutortracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class staff_login extends AppCompatActivity {
    DatabaseReference databaseReference;

    Button b_login;
    EditText et_email, et_pwd;
    String email, pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://madproj-a6ad1-default-rtdb.firebaseio.com/");

        b_login = findViewById(R.id.bt_reg_log);
        et_email = findViewById(R.id.et_email);
        et_pwd = findViewById(R.id.et_password);

        b_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = et_email.getText().toString();
                email = email.replace(".", ",");
                pwd = et_pwd.getText().toString();

                databaseReference.child("staff").child("branch").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot branchSnapshot : snapshot.getChildren()) {
                            for (DataSnapshot emailSnapshot : branchSnapshot.getChildren()) {
                                String branchEmail = emailSnapshot.getKey();
                                if (branchEmail.equals(email)) {
                                    String getPassword = emailSnapshot.child("password").getValue(String.class);
                                    if (getPassword.equals(pwd)) {
                                        String branchName = branchSnapshot.getKey();
                                        String username = emailSnapshot.child("username").getValue(String.class);
                                        Intent i = new Intent(staff_login.this, staff_home.class);
                                        i.putExtra("em", email);
                                        i.putExtra("pd", pwd);
                                        //i.putExtra("username", username);
                                        i.putExtra("branch", branchName); // Add this line to pass the branch name
                                        Toast.makeText(staff_login.this,"login successful",Toast.LENGTH_SHORT);
                                        startActivity(i);
                                        return; // Exit the method after successful login
                                    }
                                }
                            }
                        }
                        // Email not found or incorrect password
                        Toast.makeText(staff_login.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle the error case
                        Toast.makeText(staff_login.this, "Network error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        
    }
}
