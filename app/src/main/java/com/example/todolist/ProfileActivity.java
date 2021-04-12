package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
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

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener, PopupMenu.OnMenuItemClickListener {

    private TextView textViewUser;
    private Button buttonLogout, buttonAdd;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;

    private DatabaseReference  spinnerReference;

    private FirebaseUser firebaseUser;

    private Spinner spinnerCategory;
    private ArrayList<String> spinnerData;
    private ArrayAdapter<String> spinnerDataAdapter;

    public String categoryName ;
    String userID;


    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        assert firebaseUser != null;
        String userFullName = firebaseUser.getDisplayName();
        userID = firebaseUser.getUid();

        spinnerCategory = findViewById(R.id.spinnerCategory);
        fetchSpinnerData();
        spinnerDataAdapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerData);
        spinnerCategory.setAdapter(spinnerDataAdapter);
        spinnerCategory.setOnItemSelectedListener(this);


        buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(this);


        setUserName(userFullName);



        mRecyclerView = findViewById(R.id.recyclerViewTask);

    }

    public void showPopup(View v){
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menu_menu);
        popup.show();
    }


    private void fetchSpinnerData(){
        spinnerData = new ArrayList<>();
        spinnerReference = FirebaseDatabase.getInstance().getReference().child("ToDo" + userID);
        spinnerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                spinnerData.clear();
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

    private void setUserName(String userFullName) {
        textViewUser = findViewById(R.id.textViewUser);
        if (firebaseUser != null) {
            String[] userName = userFullName.split(" ");
            textViewUser.setText("Witaj " + userName[0]);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//

            case R.id.buttonAdd:
                Intent goToAdd = new Intent(ProfileActivity.this, AddActivity.class);
                goToAdd.putExtra("keyID", userID);
                goToAdd.putExtra("categoryTask", categoryName);
                startActivity(goToAdd);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spinnerCategory:
                categoryName = parent.getItemAtPosition(position).toString();
                fetchData();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void fetchData(){
        new FirebaseDatabaseHelper(categoryName).readTasks(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<com.example.todolist.Task> tasks, List<String> keys) {
                new RecyclerView_Config().setConfig(mRecyclerView, ProfileActivity.this, tasks, keys, userID);
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
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                googleSignInClient = GoogleSignIn.getClient(ProfileActivity.this, GoogleSignInOptions.DEFAULT_SIGN_IN);
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            firebaseAuth.signOut();
                            Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
                return true;

            case R.id.wieleZadan:
                Intent goToAdd = new Intent(ProfileActivity.this, dodajWiele.class);
                goToAdd.putExtra("keyID", userID);
                goToAdd.putExtra("categoryTask", categoryName);
                startActivity(goToAdd);
                return true;
        }
        return false;
    }
}