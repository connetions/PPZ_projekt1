package com.example.todolist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DoesAdapter extends RecyclerView.Adapter<DoesAdapter.MyViewHolder> {

    Context context;
    ArrayList<MyDoes> myDoes;
    DatabaseReference reference,spinnerFinish;


    public DoesAdapter(Context c, ArrayList<MyDoes> p){
        context = c;
        myDoes = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_does, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        holder.taskTitle.setText(myDoes.get(position).getTitleTask());
        holder.taskDate.setText("Data: " + myDoes.get(position).getDateTask());
        holder.taskTime.setText("Godzina: " + myDoes.get(position).getTimeTask());


        final String getTitleTask = myDoes.get(position).getTitleTask();
        final String getDateTask = myDoes.get(position).getDateTask();
        final String getTimeTask = myDoes.get(position).getTimeTask();
        final String getKeyTask = myDoes.get(position).getKeyTask();
        final String getUserID = myDoes.get(position).getUserID();
        final String getCategoryTask = myDoes.get(position).getCategoryTask();


        if(getCategoryTask.equals("Ukonczone")) {
            holder.taskCategory.setText("Kategoria: " + myDoes.get(position).getLastCategoryTask());
        }

        reference = FirebaseDatabase.getInstance().getReference().child("ToDo" + getUserID).child(getCategoryTask).child("Does" + getKeyTask);
        spinnerFinish = FirebaseDatabase.getInstance().getReference().child("ToDo" + getUserID).child("Ukonczone").child("Does" + getKeyTask);
        holder.finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(!getCategoryTask.equals("Ukonczone")) {
//                    spinnerFinish.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            snapshot.getRef().child("titleTask").setValue(getTitleTask);
//                            snapshot.getRef().child("dateTask").setValue(getDateTask);
//                            snapshot.getRef().child("timeTask").setValue(getTimeTask);
//                            snapshot.getRef().child("categoryTask").setValue("Ukonczone");
//                            snapshot.getRef().child("lastCategoryTask").setValue(getCategoryTask);
//                            snapshot.getRef().child("keyTask").setValue(getKeyTask);
//                            snapshot.getRef().child("userID").setValue(getUserID);
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                        }
//                    });
//                }
                
                reference.removeValue();
//                Intent refresh = new Intent(context, ProfileActivity.class);
//                context.startActivity(refresh);
//                notifyDataSetChanged();

            }
        });



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToEdit = new Intent(context, EditActivity.class);
                goToEdit.putExtra("titleTask", getTitleTask);
                goToEdit.putExtra("dateTask",getDateTask);
                goToEdit.putExtra("timeTask",getTimeTask);
                goToEdit.putExtra("keyTask",getKeyTask);
                goToEdit.putExtra("userID",getUserID);
                goToEdit.putExtra("categoryTask",getCategoryTask);
                context.startActivity(goToEdit);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myDoes.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView taskTitle, taskDate, taskTime,taskCategory;
        Button finishButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskDate = itemView.findViewById(R.id.taskDate);
            taskTime = itemView.findViewById(R.id.taskTime);
            finishButton = itemView.findViewById(R.id.finishButton);
            taskCategory = itemView.findViewById(R.id.taskCategory);
        }
    }
}
