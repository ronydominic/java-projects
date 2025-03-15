package com.madproject.tutortracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class student_login extends AppCompatActivity {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://madproj-a6ad1-default-rtdb.firebaseio.com/");
    Button b_notreg,b_login;
    EditText et_email,et_pwd;
    String email,pwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        b_notreg=(Button) findViewById(R.id.bt_notRegistered);
        b_login=(Button) findViewById(R.id.bt_reg_log);
        et_email=(EditText) findViewById(R.id.et_email);
        et_pwd=(EditText) findViewById(R.id.et_password);
        b_notreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(student_login.this,Student_registration.class);
                startActivity(i);
            }
        });
        b_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = et_email.getText().toString();
                email = email.replace(".", ",");
                pwd = et_pwd.getText().toString();
                databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(email)) {
                            String getPassword = snapshot.child(email).child("password").getValue(String.class);
                            if (pwd.isEmpty()) {
                                Toast.makeText(student_login.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                            } else if (getPassword.equals(pwd)) {
                                Intent i = new Intent(student_login.this, student_home.class);
                                i.putExtra("em", email);
                                i.putExtra("pd", pwd);
                                Toast.makeText(student_login.this,"login successful",Toast.LENGTH_SHORT);
                                startActivity(i);
                            } else {
                                Toast.makeText(student_login.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                            }
                        } else if (email.isEmpty() && pwd.isEmpty()) {
                            Toast.makeText(student_login.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(student_login.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(student_login.this, "Network error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



            }
    }