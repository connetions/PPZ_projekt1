package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import java.util.List;
import java.util.Random;

public class AddActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    private TextView textViewDate , textViewTime;
    private EditText editTextTask, editTextCategory;
    private Button buttonAdd, buttonDate, buttonTime;
    private DatabaseReference spinnerReference;
    private Spinner spinnerCategory;
    private ArrayAdapter<String> spinnerDataAdapter;
    private ArrayList<String> spinnerData;

    String categoryName;

    Context mContext = this;
    String keyTask = Integer.toString(new Random().nextInt());

    Calendar calendar = Calendar.getInstance();
    final int hour = calendar.get(Calendar.HOUR_OF_DAY);
    final int minute = calendar.get(Calendar.MINUTE);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);
    final int month = calendar.get(Calendar.MONTH) ;
    final int year = calendar.get(Calendar.YEAR);

    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        textViewDate = findViewById(R.id.textViewDate);
        textViewTime = findViewById(R.id.textViewTime);
        editTextTask = findViewById(R.id.editTextTask);
        editTextCategory = findViewById(R.id.editTextCategory);

//
        // datePicker okodowany
        buttonDate = findViewById(R.id.buttonDate);
        buttonDate.setOnClickListener(this);

//        time picker okodowany
        buttonTime = findViewById(R.id.buttonTime);

        buttonTime.setOnClickListener(this);

        userID = getIntent().getStringExtra("keyID");
        categoryName =  getIntent().getStringExtra("categoryTask");

        spinnerCategory = findViewById(R.id.spinnerCategory);
        fetchSpinnerData();
        spinnerDataAdapter = new ArrayAdapter<String>(AddActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerData);
        spinnerCategory.setAdapter(spinnerDataAdapter);
        spinnerCategory.setOnItemSelectedListener(this);

        buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(this);
    }

    private void fetchSpinnerData(){
        spinnerData = new ArrayList<>();
        spinnerReference = FirebaseDatabase.getInstance().getReference().child("ToDo" + userID);
        spinnerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    spinnerData.add(snapshot1.getKey());
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
        switch (v.getId()){
            case R.id.buttonAdd:

                Task task = new Task();
                task.setTitleTask(editTextTask.getText().toString());
                task.setDateTask(textViewDate.getText().toString());
                task.setTimeTask(textViewTime.getText().toString());
                task.setCategoryTask(editTextCategory.getText().toString());
                task.setUserID(userID);
                task.setKeyTask(keyTask);

                new FirebaseDatabaseHelper(editTextCategory.getText().toString()).addTask(task, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Task> tasks, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {
                        Toast.makeText(mContext, "Suscces", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });



                //nazwa bazki + ID usera i podrzewa does + losowy numer taska
//                reference = FirebaseDatabase.getInstance().getReference().child("ToDo" + userID).child(editTextCategory.getText().toString()).child("Does" + keyTask);
//                reference.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        snapshot.getRef().child("titleTask").setValue(editTextTask.getText().toString());
//                        snapshot.getRef().child("dateTask").setValue(textViewDate.getText());
//                        snapshot.getRef().child("timeTask").setValue(textViewTime.getText());
//                        snapshot.getRef().child("categoryTask").setValue(editTextCategory.getText().toString());
//                        snapshot.getRef().child("keyTask").setValue(keyTask);
//                        snapshot.getRef().child("userID").setValue(userID);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                    }
//                });

//                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//                    NotificationChannel channel = new NotificationChannel("My","My", NotificationManager.IMPORTANCE_DEFAULT);
//                    NotificationManager manager = getSystemService(NotificationManager.class);
//                    manager.createNotificationChannel(channel);
//                }
//
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(AddActivity.this, "My")
//                        .setSmallIcon(android.R.drawable.ic_dialog_info)
//                        .setContentTitle("I'ts time")
//                        .setContentText("XDDDD")
//                        .setAutoCancel(true);
//
//                Intent intent = new Intent(AddActivity.this, ProfileActivity.class);
//
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                PendingIntent pendingIntent = PendingIntent.getActivity(AddActivity.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//                builder.setContentIntent(pendingIntent);
//
//                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(AddActivity.this);
//                notificationManager.notify(1,builder.build());
//
//                String[] hourTime = textViewTime.getText().toString().split(":");
//                int hhour = Integer.parseInt(hourTime[0]);
//                int mminute = Integer.parseInt(hourTime[1]);

//                Intent backToProfile = new Intent(AddActivity.this, ProfileActivity.class);
//                startActivity(backToProfile);
                break;

            case R.id.buttonDate:
                DatePickerDialog datePickerDialog;
                datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date =  month + 1 + "/" + dayOfMonth + "/" + year;
                        textViewDate.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
                break;

            case R.id.buttonTime:
                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        textViewTime.setText((hourOfDay + ":" + minute));
                    }
                },hour, minute,true);
                timePickerDialog.show();
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
}