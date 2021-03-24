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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class AddActivity extends AppCompatActivity  {

    TextView dateView , timeView;
    EditText taskEdit, categoryEdit;
    Button addButton, dateButton, timeButton;
    DatabaseReference reference,spinnerReference;
//    String doesID = new ProfileActivity().userID;
    Spinner spinner;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinerData;
    ValueEventListener listener;
    String categoryName;

    Context mContext = this;
    String keyTask = Integer.toString(new Random().nextInt());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        dateView = findViewById(R.id.dateView);
        timeView = findViewById(R.id.timeView);
        taskEdit = findViewById(R.id.taskEdit);
        categoryEdit = findViewById(R.id.categoryEdit);

//      Inicjalizowanie zmiennych do kalendarza i czasu
        Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH) ;
        final int year = calendar.get(Calendar.YEAR);



        taskEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("...".equals(taskEdit.getText().toString()) ){
                    taskEdit.setText("");
                }
            }
        });

        categoryEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("Podaj kategorie".equals(categoryEdit.getText().toString()) ){
                    categoryEdit.setText("");
                }
            }
        });
        // datePicker okodowany
        dateButton = findViewById(R.id.dateButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog;
                datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date =  month + 1 + "/" + dayOfMonth + "/" + year;
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

//        Dodawanie do bazy

        final String userID = getIntent().getStringExtra("keyID");
        categoryName =  getIntent().getStringExtra("categoryTask");
        spinner=findViewById(R.id.categorySpinner);
        spinnerReference = FirebaseDatabase.getInstance().getReference().child("ToDo" + userID);
        spinerData = new ArrayList<>();
        adapter = new ArrayAdapter<String>(AddActivity.this,android.R.layout.simple_spinner_dropdown_item,spinerData);

        listener = spinnerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren()) {
                    spinerData.add(snapshot1.getKey());
                }
                adapter = new ArrayAdapter<String>(AddActivity.this,android.R.layout.simple_spinner_dropdown_item,spinerData);
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

        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data_checker()){
//                nazwa bazki + ID usera i podrzewa does + losowy numer taska
                    reference = FirebaseDatabase.getInstance().getReference().child("ToDo" + userID).child(categoryEdit.getText().toString()).child("Does" + keyTask);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            snapshot.getRef().child("titleTask").setValue(taskEdit.getText().toString());
                            snapshot.getRef().child("dateTask").setValue(dateView.getText());
                            snapshot.getRef().child("timeTask").setValue(timeView.getText());
                            snapshot.getRef().child("categoryTask").setValue(categoryEdit.getText().toString());
                            snapshot.getRef().child("keyTask").setValue(keyTask);
                            snapshot.getRef().child("userID").setValue(userID);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                    Intent backToProfile = new Intent(AddActivity.this, ProfileActivity.class);
                    backToProfile.putExtra("categoryTask", categoryName);
                    startActivity(backToProfile);
//                finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Złe dane", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    boolean data_checker(){
        int cnt = 0;
        if (taskEdit.getText().toString().equals("...")) {
            cnt++;
        }
        if (dateView.getText().toString().equals("Wybrana data")) {
            cnt++;
        }
        if (timeView.getText().toString().equals("Wybrana godzina")){
            cnt++;
        }

        if (categoryEdit.getText().toString().equals("Podaj kategorie")){
            cnt++;
        }

        if (cnt == 0){
            return true;
        }

        return false;
        }

}