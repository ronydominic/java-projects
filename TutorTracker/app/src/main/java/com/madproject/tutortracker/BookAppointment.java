package com.madproject.tutortracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;

public class BookAppointment extends AppCompatActivity {

    private DatabaseReference branchRef;
    private TextView tv_req_stat;
    private Button b_request;
    private EditText e_reason;
    private String stud_email;
    private String tutor_email;
    private String branch;
    private EditText ed_from;
    private EditText ed_to;
    private EditText ed_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        branchRef = FirebaseDatabase.getInstance().getReference().child("staff").child("branch");
        tv_req_stat = findViewById(R.id.tv_request_status);
        b_request = findViewById(R.id.bt_request);
        e_reason = findViewById(R.id.ed_reason);
        ed_from = findViewById(R.id.ed_from);
        ed_to = findViewById(R.id.ed_to);
        ed_name = findViewById(R.id.ed_name);
        stud_email = getIntent().getStringExtra("stud_email");
        tutor_email = getIntent().getStringExtra("em_tutor");
        branch = getIntent().getStringExtra("sb");

        ed_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(ed_from);
            }
        });

        ed_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(ed_to);
            }
        });

        b_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fromTime = ed_from.getText().toString();
                String toTime = ed_to.getText().toString();

                if (fromTime.isEmpty() || toTime.isEmpty()) {
                    // Handle case when either "from" or "to" time is not selected
                    Toast.makeText(BookAppointment.this, "Please select both 'from' and 'to' times.", Toast.LENGTH_SHORT).show();
                } else if (isToTimeBeforeFromTime(fromTime, toTime)) {
                    // Handle case when "to" time is before "from" time
                    Toast.makeText(BookAppointment.this, "'To' time cannot be before 'From' time.", Toast.LENGTH_SHORT).show();
                    ed_to.setText(""); // Clear the "to" time EditText
                } else {
                    // Both times are valid, proceed with database update
                    // ...

                    branchRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            DatabaseReference db = snapshot.child(branch).child(tutor_email).child("appointments").child(stud_email).getRef();
                            db.child("purpose").setValue(e_reason.getText().toString());
                            db.child("username").setValue(ed_name.getText().toString());
                            db.child("from").setValue(fromTime);
                            db.child("to").setValue(toTime);
                            db.child("request_status").setValue("").addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(BookAppointment.this, "Booked successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle onCancelled event if needed
                        }
                    });

// ...

                }
            }
        });



        branchRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String req_stat = snapshot.child(branch).child(tutor_email).child("appointments").child(stud_email).child("request_status").getValue(String.class);
                if (req_stat != null) {
                    if (req_stat.equals("approved")) {
                        tv_req_stat.setText("REQUEST APPROVED");
                        tv_req_stat.setTextColor(Color.GREEN);
                    } else if (req_stat.equals("denied")) {
                        tv_req_stat.setText("REQUEST DENIED");
                        tv_req_stat.setTextColor(Color.RED);
                    } else {
                        tv_req_stat.setText("");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event if needed
            }
        });
    }
    private boolean isToTimeBeforeFromTime(String fromTime, String toTime) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        try {
            Date from = format.parse(fromTime);
            Date to = format.parse(toTime);
            return to.before(from);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showTimePickerDialog(final EditText editText) {
        // Get current time
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a time picker dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Convert selected time to 12-hour format
                        String time = convertTo12HourFormat(hourOfDay, minute);
                        editText.setText(time);
                    }
                },
                hour,
                minute,
                false // Set is24HourView to false for 12-hour format
        );

        // Show the time picker dialog
        timePickerDialog.show();
    }

    private String convertTo12HourFormat(int hourOfDay, int minute) {
        String format;
        if (hourOfDay == 0) {
            hourOfDay = 12;
            format = "AM";
        } else if (hourOfDay == 12) {
            format = "PM";
        } else if (hourOfDay > 12) {
            hourOfDay -= 12;
            format = "PM";
        } else {
            format = "AM";
        }
        return String.format(Locale.getDefault(), "%02d:%02d %s", hourOfDay, minute, format);
    }

    private int getSelectedHour(String time) {
        String[] parts = time.split(":");
        String[] timeParts = parts[0].split(" ");
        int hour = Integer.parseInt(timeParts[0]);
        if (timeParts[1].equalsIgnoreCase("PM") && hour < 12) {
            hour += 12;
        }
        return hour;
    }

    private int getSelectedMinute(String time) {
        String[] parts = time.split(":");
        return Integer.parseInt(parts[1].substring(0, 2));
    }
}
