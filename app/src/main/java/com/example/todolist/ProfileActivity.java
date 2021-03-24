package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity  {

    TextView userView;
    Button logoutButton, addButton;
    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;

    DatabaseReference reference, spinnerReference;
    RecyclerView ourdoes;
    ArrayList<MyDoes> list;
    DoesAdapter doesAdapter;

    Spinner spinner;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinerData;
    ValueEventListener listener;
    String categoryName;

    public String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        spinner=findViewById(R.id.categorySpinner);
        spinnerReference = FirebaseDatabase.getInstance().getReference().child("ToDo" + userID);
        spinerData = new ArrayList<>();
        adapter = new ArrayAdapter<String>(ProfileActivity.this,android.R.layout.simple_spinner_dropdown_item,spinerData);

//        spinner.setAdapter(adapter);
//        retrievData();


        listener = spinnerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren()) {
                    spinerData.add(snapshot1.getKey());
                }
                adapter = new ArrayAdapter<String>(ProfileActivity.this,android.R.layout.simple_spinner_dropdown_item,spinerData);
                spinner.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                categoryName = parent.getItemAtPosition(position).toString();
//               categoryName = getIntent().getStringExtra("categoryTask");
                fetchData();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


//      Przycisk przejscia do dodawania taskow
        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToAdd = new Intent(ProfileActivity.this,AddActivity.class);
                goToAdd.putExtra("keyID", userID);
                goToAdd.putExtra("categoryTask", categoryName);
                startActivity(goToAdd);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        userView = findViewById(R.id.userView);
        if (firebaseUser != null){
            String userFullName = firebaseUser.getDisplayName();
            String[] userName = userFullName.split(" ");
            userView.setText("Witaj " + userName[0]);
        }

        googleSignInClient = GoogleSignIn.getClient(ProfileActivity.this, GoogleSignInOptions.DEFAULT_SIGN_IN);
//        wylogowywanie sie
        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            firebaseAuth.signOut();
                            Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();
//                            finish();
                            Intent backToMain = new Intent(ProfileActivity.this,MainActivity.class);
                            startActivity(backToMain);

                        }
                    }
                });
            }
        });


    }
    public void fetchData(){
        ourdoes = findViewById(R.id.ourdoes);
        ourdoes.setLayoutManager(new LinearLayoutManager(this));
        ourdoes.setHasFixedSize(true);
        list = new ArrayList<MyDoes>();

        //get data from base

        reference = FirebaseDatabase.getInstance().getReference().child("ToDo"+userID).child(categoryName);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    MyDoes p = dataSnapshot1.getValue(MyDoes.class);
                    list.add(p);
                }
                doesAdapter = new DoesAdapter(ProfileActivity.this, list);
                ourdoes.setAdapter(doesAdapter);
                doesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }


    }



//    public void retrievData(){
//        listener = spinnerReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot snapshot1: snapshot.getChildren()) {
//                    spinerData.add(snapshot1.getValue().toString());
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }


