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

public class EditActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    private TextView textViewTime, textViewDate;
    EditText editTextTask, editTextCategory;
    private Button buttonUpdate, buttonDelete, buttonDate, buttonTime;
    DatabaseReference reference, spinnerReference;
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

    private String keykeyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        textViewDate = findViewById(R.id.textViewDate);
        textViewTime = findViewById(R.id.textViewTime);
        editTextTask = findViewById(R.id.editTextTask);
        editTextCategory = findViewById(R.id.editTextCategory);

        editTextTask.setText(getIntent().getStringExtra("titleTask"));
        textViewTime.setText(getIntent().getStringExtra("timeTask"));
        textViewDate.setText(getIntent().getStringExtra("dateTask"));
        editTextCategory.setText(getIntent().getStringExtra("categoryTask"));

        categoryName = getIntent().getStringExtra("categoryTask");

        final String keykeyDoes = getIntent().getStringExtra("keyTask");
        keykeyID = getIntent().getStringExtra("userID");

        reference = FirebaseDatabase.getInstance().getReference().child("ToDo" + keykeyID).child(getIntent().getStringExtra("categoryTask")).child("Does" + keykeyDoes);


        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(this);

        buttonDelete = findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(this);

        // datePicker okodowany
        buttonDate = findViewById(R.id.buttonDate);
        buttonDate.setOnClickListener(this);

//        time picker okodowany
        buttonTime = findViewById(R.id.buttonTime);
        buttonTime.setOnClickListener(this);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        fetchSpinnerData();

        spinnerDataAdapter = new ArrayAdapter<String>(EditActivity.this, android.R.layout.simple_spinner_dropdown_item, spinerData);
        spinnerCategory.setAdapter(spinnerDataAdapter);

        spinnerCategory.setOnItemSelectedListener(this);


        spinnerCategory.setOnItemSelectedListener(this);
    }


    private void fetchSpinnerData() {
        spinerData = new ArrayList<>();
        spinnerReference = FirebaseDatabase.getInstance().getReference().child("ToDo" + keykeyID);
        spinnerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    spinerData.add(snapshot1.getKey());
                }
                spinnerDataAdapter.notifyDataSetChanged();
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
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().child("titleTask").setValue(editTextTask.getText().toString());
                        snapshot.getRef().child("dateTask").setValue(textViewDate.getText());
                        snapshot.getRef().child("timeTask").setValue(textViewTime.getText());
                        snapshot.getRef().child("categoryTask").setValue(editTextCategory.getText().toString());

                        Intent backToProfile = new Intent(EditActivity.this, ProfileActivity.class);
                        backToProfile.putExtra("categoryTask", categoryName);
                        startActivity(backToProfile);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case R.id.buttonDelete:
                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent goToProfile = new Intent(EditActivity.this, ProfileActivity.class);
                            startActivity(goToProfile);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
                editTextCategory.setText(categoryName);
                categoryName = parent.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}