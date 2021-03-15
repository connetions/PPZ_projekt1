package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Random;

public class AddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView dateView;
    private TextView timeView;
    EditText taskEdit;
    Button buttonAddTask;
    DatabaseReference reference;
    String doesID = new ProfileActivity().userID;
    Integer doesNumber = new Random().nextInt();
    String dateSet, timeSet;
    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        dateView = findViewById(R.id.dateView);
        timeView = findViewById(R.id.timeView);

        Calendar calendar = Calendar.getInstance();

        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        findViewById(R.id.dateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDailog();
            }
        });

        findViewById(R.id.timeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeView.setText((hourOfDay + ":" + minute));
                        timeSet = hourOfDay + ":" + minute;
                    }
                },hour, minute,android.text.format.DateFormat.is24HourFormat(mContext));
                timePickerDialog.show();
            }
        });


        findViewById(R.id.buttonAddTask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference().child("ToDo").child("Does" + doesNumber);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().child("tittleDoes").setValue("XD");
                        snapshot.getRef().child("descDoes").setValue("taskEdit.getText().toString()");
                        snapshot.getRef().child("dataDoes").setValue("taskEdit.getText().toString()");
                        Intent a = new Intent(AddActivity.this,ProfileActivity.class);
                        startActivity(a);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }


    private void showDatePickerDailog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,this, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = "month/day/year:" + month + "/" + dayOfMonth + "/" + year;
        dateSet = date;
        dateView.setText(date);
    }
}