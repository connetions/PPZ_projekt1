package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class EditActivity extends AppCompatActivity {

    private TextView dateView , timeView;
    EditText taskEdit, categoryEdit;
    Button updateButton, deleteButton, dateButton, timeButton;
    DatabaseReference reference,spinnerReference;
    Context mContext = this;

    Spinner spinner;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinerData;
    ValueEventListener listener;
    String categoryName ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        dateView = findViewById(R.id.dateView);
        timeView = findViewById(R.id.timeView);
        taskEdit = findViewById(R.id.taskEdit);
        categoryEdit = findViewById(R.id.categoryEdit);

        deleteButton = findViewById(R.id.deleteButton);
        updateButton = findViewById(R.id.updateButton);

        taskEdit.setText(getIntent().getStringExtra("titleTask"));
        timeView.setText(getIntent().getStringExtra("timeTask"));
        dateView.setText(getIntent().getStringExtra("dateTask"));
        categoryEdit.setText(getIntent().getStringExtra("categoryTask"));
        categoryName = getIntent().getStringExtra("categoryTask");

        final String keykeyDoes = getIntent().getStringExtra("keyTask");
        final String keykeyID = getIntent().getStringExtra("userID");

        reference = FirebaseDatabase.getInstance().getReference().child("ToDo" + keykeyID).child(getIntent().getStringExtra("categoryTask")).child("Does" + keykeyDoes);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().child("titleTask").setValue(taskEdit.getText().toString());
                        snapshot.getRef().child("dateTask").setValue(dateView.getText());
                        snapshot.getRef().child("timeTask").setValue(timeView.getText());
                        snapshot.getRef().child("categoryTask").setValue(categoryEdit.getText().toString());

                        Intent backToProfile = new Intent(EditActivity.this, ProfileActivity.class);
                        backToProfile.putExtra("categoryTask", categoryName);
                        startActivity(backToProfile);

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent goToProfile = new Intent(EditActivity.this, ProfileActivity.class);
                            startActivity(goToProfile);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);


        // datePicker okodowany
        dateButton = findViewById(R.id.dateButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog;
                datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = month + "/" + dayOfMonth + "/" + year;
                        dateView.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

//        time picker okodowany
        timeButton = findViewById(R.id.timeButton);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeView.setText((hourOfDay + ":" + minute));
                    }
                },hour, minute,true);
                timePickerDialog.show();
            }
        });

        spinner=findViewById(R.id.categorySpinner);
        spinnerReference = FirebaseDatabase.getInstance().getReference().child("ToDo" + keykeyID);
        spinerData = new ArrayList<>();
        adapter = new ArrayAdapter<String>(EditActivity.this,android.R.layout.simple_spinner_dropdown_item,spinerData);

        listener = spinnerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren()) {
                    spinerData.add(snapshot1.getKey());
                }
                adapter = new ArrayAdapter<String>(EditActivity.this,android.R.layout.simple_spinner_dropdown_item,spinerData);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                categoryEdit.setText(categoryName);
                categoryName = parent.getItemAtPosition(position).toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}