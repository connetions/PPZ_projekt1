package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class EditActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    private TextView textViewTime, textViewDate;
    EditText editTextTask, editTextCategory;
    private Button buttonUpdate, buttonDelete, buttonDate, buttonTime;
    DatabaseReference  spinnerReference;
    Context mContext = this;

    Spinner spinnerCategory;
    ArrayAdapter<String> spinnerDataAdapter;
    ArrayList<String> spinerData;

    String categoryName;

    Calendar calendar = Calendar.getInstance();
    final int hour = calendar.get(Calendar.HOUR_OF_DAY);
    final int minute = calendar.get(Calendar.MINUTE);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);
    final int month = calendar.get(Calendar.MONTH);
    final int year = calendar.get(Calendar.YEAR);

    private String key;
    private String title;
    private String date;
    private String time;
    private String category;
    private String userID;

    public int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        key = getIntent().getStringExtra("key");
        title = getIntent().getStringExtra("title");
        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");
        category = getIntent().getStringExtra("category");

        userID = getIntent().getStringExtra("userID");

        textViewDate = findViewById(R.id.textViewDate);
        textViewTime = findViewById(R.id.textViewTime);
        editTextTask = findViewById(R.id.editTextTask);
        editTextCategory = findViewById(R.id.editTextCategory);

        editTextTask.setText(title);
        textViewTime.setText(time);
        textViewDate.setText(date);
        editTextCategory.setText(category);
//
//
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(this);

        buttonDelete = findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(this);


        buttonDate = findViewById(R.id.buttonDate);
        buttonDate.setOnClickListener(this);


        buttonTime = findViewById(R.id.buttonTime);
        buttonTime.setOnClickListener(this);

        spinnerCategory = findViewById(R.id.spinnerCategory);

        fetchSpinnerData();

        spinnerDataAdapter = new ArrayAdapter<String>(EditActivity.this, android.R.layout.simple_spinner_dropdown_item,spinerData);
        spinnerCategory.setAdapter(spinnerDataAdapter);
        spinnerCategory.setOnItemSelectedListener(this);

    }


    public void fetchSpinnerData() {
        spinerData = new ArrayList<String>();
        spinnerReference = FirebaseDatabase.getInstance().getReference().child("ToDo" + userID);

        spinnerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    spinerData.add(snapshot1.getKey().toString());

                }
                getIndexSpinnerItem(spinerData, category);
                spinnerDataAdapter.notifyDataSetChanged();
                spinnerCategory.setSelection(index);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonUpdate:
                Task task = new Task();
                task.setTitleTask(editTextTask.getText().toString());
                task.setDateTask(textViewDate.getText().toString());
                task.setTimeTask(textViewTime.getText().toString());
                task.setCategoryTask(editTextCategory.getText().toString());
//                task.setUserID(userID);
//                task.setKeyTask(keyTask);
                if (!category.equals(editTextCategory.getText().toString())){
                    new FirebaseDatabaseHelper(editTextCategory.getText().toString()).addTask(task, new FirebaseDatabaseHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(List<Task> tasks, List<String> keys) {

                        }

                        @Override
                        public void DataIsInserted() {


                        }

                        @Override
                        public void DataIsUpdated() {

                        }

                        @Override
                        public void DataIsDeleted() {

                        }
                    });

                    new FirebaseDatabaseHelper(category).deleteTask(key, new FirebaseDatabaseHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(List<Task> tasks, List<String> keys) {

                        }

                        @Override
                        public void DataIsInserted() {

                        }

                        @Override
                        public void DataIsUpdated() {

                        }

                        @Override
                        public void DataIsDeleted() {
                            Toast.makeText(mContext, "Finish", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    new FirebaseDatabaseHelper(category).updateTask(key, task, new FirebaseDatabaseHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(List<Task> tasks, List<String> keys) {

                        }

                        @Override
                        public void DataIsInserted() {

                        }

                        @Override
                        public void DataIsUpdated() {
                            Toast.makeText(mContext, "Update", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void DataIsDeleted() {

                        }
                    });
                }
                finish();
                break;

            case R.id.buttonDelete:
                    new FirebaseDatabaseHelper(category).deleteTask(key, new FirebaseDatabaseHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(List<Task> tasks, List<String> keys) {
                            
                        }

                        @Override
                        public void DataIsInserted() {

                        }

                        @Override
                        public void DataIsUpdated() {

                        }

                        @Override
                        public void DataIsDeleted() {
                            Toast.makeText(mContext, "Delete", Toast.LENGTH_SHORT).show();
                        }
                    });
                finish();
                break;

            case R.id.buttonDate:
                DatePickerDialog datePickerDialog;
                datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = month + "/" + dayOfMonth + "/" + year;
                        textViewDate.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
                break;
            case R.id.buttonTime:
                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        textViewTime.setText((hourOfDay + ":" + minute));
                    }
                }, hour, minute, true);
                timePickerDialog.show();
                break;

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinnerCategory:
                categoryName = parent.getItemAtPosition(position).toString();
                editTextCategory.setText(categoryName);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void getIndexSpinnerItem(ArrayList<String> spinner, String item){

        for (int i = 0; i < spinner.size(); i++){
            if(spinner.get(i).equals(item)){
                index = i;

                break;
            }
        }


    }

}