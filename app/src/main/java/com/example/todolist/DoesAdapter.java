package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DoesAdapter extends RecyclerView.Adapter<DoesAdapter.MyViewHolder> {

    Context context;
    ArrayList<MyDoes> myDoes;

    public DoesAdapter(Context c, ArrayList<MyDoes> p){
        context = c;
        myDoes = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_does, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.taskTitle.setText(myDoes.get(position).getTitleTask());
        holder.taskDate.setText(myDoes.get(position).getDateTask());
        holder.taskTime.setText(myDoes.get(position).getTimeTask());


        final String getTitleTask = myDoes.get(position).getTitleTask();
        final String getDateTask = myDoes.get(position).getDateTask();
        final String getTimeTask = myDoes.get(position).getTimeTask();
        final String getKeyTask = myDoes.get(position).getKeyTask();
        final String getUserID = myDoes.get(position).getUserID();
        final String getCategoryTask = myDoes.get(position).getCategoryTask();

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

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView taskTitle, taskDate, taskTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskDate = itemView.findViewById(R.id.taskDate);
            taskTime = itemView.findViewById(R.id.taskTime);

        }
    }
}
