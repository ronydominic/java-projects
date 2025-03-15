package com.madproject.tutortracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Student_registration extends AppCompatActivity {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://madproj-a6ad1-default-rtdb.firebaseio.com/");
    Button register;
    EditText ed_email,ed_password;
    private String email="",password="";
    TextView emailwarning,pass_warning;
    private static final String pwd_az=".*[a-z].*";
    private static final String pwd_AZ=".*[A-Z].*";
    private static final String pwd_num=".*[0-9].*";
    private static final String pwd_sp=".*[%,@,#,$,%,!,+].*";
    private static final String email_Reg="^[a-zA-Z0-9_.+-]*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z]{2,}$";
    Pattern mail_pattern,pwd_az_pattern,pwd_AZ_pattern,pwd_num_pattern,pwd_sp_pattern;
    Matcher mail_matcher,pwd_az_matcher,pwd_AZ_matcher,pwd_num_matcher,pwd_sp_matcher;

    public Student_registration() {
    }


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);
        register=(Button) findViewById(R.id.bt_reg_log);
        ed_email=(EditText) findViewById(R.id.et_email);
        ed_password=(EditText) findViewById(R.id.et_password);
        emailwarning=(TextView) findViewById(R.id.tv_email_warning);
        pass_warning=(TextView) findViewById(R.id.tv_pwd_warning);
        mail_pattern=Pattern.compile(email_Reg);
        pwd_AZ_pattern=Pattern.compile(pwd_AZ);
        pwd_az_pattern=Pattern.compile(pwd_az);
        pwd_num_pattern=Pattern.compile(pwd_num);
        pwd_sp_pattern=Pattern.compile(pwd_sp);
        ed_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //String a = s.toString();
                mail_matcher=mail_pattern.matcher(s);
                if(!mail_matcher.matches())
                {
                    emailwarning.setText("Enter a valid email id");
                }
                else
                {

                    email=s.toString();
                    emailwarning.setText("");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        ed_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //String a = s.toString();
                pwd_AZ_matcher=pwd_AZ_pattern.matcher(s);
                pwd_az_matcher=pwd_az_pattern.matcher(s);
                pwd_num_matcher=pwd_num_pattern.matcher(s);
                pwd_sp_matcher=pwd_sp_pattern.matcher(s);
                int string_length=s.length();
                if(!(string_length >5))
                {
                    pass_warning.setText("   password length too short");
                }
                else if (!pwd_AZ_matcher.matches()) {
                    pass_warning.setText("   should contain upper case");
                } else if (!pwd_az_matcher.matches()) {
                    pass_warning.setText("   should contain lower case");
                } else if (!pwd_num_matcher.matches()) {
                    pass_warning.setText("   must contain number");
                } else if (!pwd_sp_matcher.matches()) {
                    pass_warning.setText("  must contain special char %,@,#,$,%,!,+");
                }
                else
                {
                    pass_warning.setText("");
                    password= s.toString();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.isBlank())
                {
                    emailwarning.setText("Enter valid details");
                    Toast.makeText(Student_registration.this,"Invalid details entered",Toast.LENGTH_SHORT).show();
                }
                if ( password.isBlank())
                {
                    pass_warning.setText("        Enter valid details");
                    Toast.makeText(Student_registration.this,"Invalid details entered",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String emailKey = email.replace(".", ",");
                            if(snapshot.hasChild(emailKey))
                            {
                                Toast.makeText(Student_registration.this,"email is already registered",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {

                                databaseReference.child("users").child(emailKey).child("password").setValue(password);
                                Toast.makeText(Student_registration.this,"user registration successful",Toast.LENGTH_SHORT).show();
                                finish();
                                Intent i = new Intent(Student_registration.this,student_login.class);
                                i.putExtra("em",email);
                                i.putExtra("pd",password);
                                startActivity(i);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }
        });


    }
}