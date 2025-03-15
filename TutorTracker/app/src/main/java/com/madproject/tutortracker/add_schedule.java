package com.madproject.tutortracker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class add_schedule extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://madproj-a6ad1-default-rtdb.firebaseio.com/");

    private LinearLayout containerLayout;
    private Button bUpdate;
    private Button bClear;
    private Button b_1;
    private ArrayList<String> al;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        containerLayout = findViewById(R.id.containerLayout);
        bUpdate = findViewById(R.id.b_update);
        bClear = findViewById(R.id.b_clear);
        b_1 = findViewById(R.id.b_1);
        al = new ArrayList<>();
        Intent intent = getIntent();
        String email = intent.getStringExtra("em");
        CharSequence staffBranch = intent.getCharSequenceExtra("branch");

        b_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });

        bUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference scheduleRef = databaseReference.child("staff").child("branch").child((String) staffBranch).child(email);
                scheduleRef.child("schedule").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Schedule node removed successfully
                        Toast.makeText(add_schedule.this, "Schedule removed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to remove schedule node
                        Toast.makeText(add_schedule.this, "Failed to remove schedule", Toast.LENGTH_SHORT).show();
                    }
                });




                DatabaseReference demoRef = databaseReference.child("staff").child("branch").child("" + staffBranch).child("" + email).child("schedule");

                boolean isValid = true; // Flag to track if all tasks have valid times

                for (String uid : al) {
                    View taskView = containerLayout.findViewWithTag(uid);
                    if (taskView != null) {
                        EditText edT = taskView.findViewById(R.id.ed_t);
                        EditText ed1 = taskView.findViewById(R.id.ed_1);
                        EditText ed2 = taskView.findViewById(R.id.ed_2);
                        EditText ed3 = taskView.findViewById(R.id.ed_3);

                        String task = edT.getText().toString();
                        String from = ed1.getText().toString();
                        String to = ed2.getText().toString();
                        String date = ed3.getText().toString();

                        if (!isTimeValid(from, to)) {
                            isValid = false;
                            break;
                        }

                        DatabaseReference taskRef = demoRef.child(uid);
                        taskRef.child("task").setValue(task);
                        taskRef.child("from").setValue(from);
                        taskRef.child("to").setValue(to);
                        taskRef.child("date").setValue(date);
                    }
                }

                if (isValid) {
                    Toast.makeText(add_schedule.this, "Updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(add_schedule.this, "To time should be greater than from time", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearTasks();
            }
        });

        // Populate the layout with existing tasks from the database
        DatabaseReference demoRef = databaseReference.child("staff").child("branch").child("" + staffBranch).child("" + email).child("schedule");

        demoRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                        String uid = taskSnapshot.getKey();
                        String task = taskSnapshot.child("task").getValue(String.class);
                        String from = taskSnapshot.child("from").getValue(String.class);
                        String to = taskSnapshot.child("to").getValue(String.class);
                        String date = taskSnapshot.child("date").getValue(String.class);

                        LayoutInflater inflater = LayoutInflater.from(add_schedule.this);
                        View taskView = inflater.inflate(R.layout.task_item, containerLayout, false);
                        taskView.setTag(uid);

                        EditText edT = taskView.findViewById(R.id.ed_t);
                        EditText ed1 = taskView.findViewById(R.id.ed_1);
                        EditText ed2 = taskView.findViewById(R.id.ed_2);
                        EditText ed3 = taskView.findViewById(R.id.ed_3);
                        ImageView icClose = taskView.findViewById(R.id.icon_close);

                        edT.setText(task);
                        ed1.setText(from);
                        ed2.setText(to);
                        ed3.setText(date);

                        icClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String uidToRemove = (String) taskView.getTag();
                                removeTask(uidToRemove);
                            }
                        });

                        containerLayout.addView(taskView);
                        al.add(uid);
                    }
                }
            }
        });
    }

    private void addTask() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View taskView = inflater.inflate(R.layout.task_item, containerLayout, false);

        final String uid = generateUniqueId();
        al.add(uid);
        taskView.setTag(uid);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;  // Month is zero-based, so add 1
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String currentDate = String.format(Locale.getDefault(), "%02d/%02d/%d", day, month, year);

        final EditText edT = taskView.findViewById(R.id.ed_t);
        final EditText ed1 = taskView.findViewById(R.id.ed_1);
        final EditText ed2 = taskView.findViewById(R.id.ed_2);
        final EditText ed3 = taskView.findViewById(R.id.ed_3);
        ed3.setText(currentDate);
        ImageView icClose = taskView.findViewById(R.id.icon_close);

        ed1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(ed1);
            }
        });

        ed2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(ed2);
            }
        });

        ed3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(ed3);
            }
        });

        icClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uidToRemove = (String) taskView.getTag();
                removeTask(uidToRemove);
            }
        });

        containerLayout.addView(taskView);
    }

    private void showTimePickerDialog(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time;
                        String amPm;

                        if (hourOfDay >= 12) {
                            amPm = "PM";
                            if (hourOfDay > 12) {
                                hourOfDay -= 12;
                            }
                        } else {
                            amPm = "AM";
                            if (hourOfDay == 0) {
                                hourOfDay = 12;
                            }
                        }

                        time = String.format(Locale.getDefault(), "%02d:%02d %s", hourOfDay, minute, amPm);

                        editText.setText(time);
                    }
                }, hourOfDay, minute, false);

        timePickerDialog.show();
    }


    private void showDatePickerDialog(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month += 1;
                        String date = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, month, year);
                        editText.setText(date);
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    private void removeTask(String uid) {
        for (int i = 0; i < containerLayout.getChildCount(); i++) {
            View taskView = containerLayout.getChildAt(i);
            if (taskView.getTag() != null && taskView.getTag().equals(uid)) {
                containerLayout.removeView(taskView);
                al.remove(uid);
                break;
            }
        }
    }

    private void clearTasks() {
        containerLayout.removeAllViews();
        al.clear();
    }

    private String generateUniqueId() {
        return String.valueOf(System.currentTimeMillis());
    }

    private boolean isTimeValid(String fromTime, String toTime) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        try {
            Date from = format.parse(fromTime);
            Date to = format.parse(toTime);
            return from.before(to);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


}
