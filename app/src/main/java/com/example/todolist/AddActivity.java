package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView dateView;
    private TextView timeView;

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
                    }
                },hour, minute,android.text.format.DateFormat.is24HourFormat(mContext));
                timePickerDialog.show();
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
        dateView.setText(date);
    }
}