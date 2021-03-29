package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerView_Config {
    private Context mContext;
    private TasksAdapter mTaskAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, List<Task> tasks, List<String> keys, String userID){
        mContext = context;
        mTaskAdapter = new TasksAdapter(tasks, keys, userID);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mTaskAdapter);

    }


    class TaskItemView extends RecyclerView.ViewHolder{
        private TextView mTitle, mTime, mDate, mCategory;

        private String key;
        private  String userID;

        public TaskItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext).inflate(R.layout.task_list_item, parent, false));

            mTitle = itemView.findViewById(R.id.taskTitle);
            mTime = itemView.findViewById(R.id.taskTime);
            mDate = itemView.findViewById(R.id.taskDate);
            mCategory = itemView.findViewById(R.id.taskCategory);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent intent = new Intent(mContext, EditActivity.class);
                    intent.putExtra("key", key);
                    intent.putExtra("userID", userID);
                    intent.putExtra("title", mTitle.getText().toString());
                    intent.putExtra("date", mDate.getText().toString());
                    intent.putExtra("time", mTime.getText().toString());
                    intent.putExtra("category", mCategory.getText().toString());
                    mContext.startActivity(intent);
                    return false;
                }
            });
        }

        public void bind(Task task, String key, String userID) {
            mTitle.setText(task.getTitleTask());
            mTime.setText(task.getTimeTask());
            mDate.setText(task.getDateTask());
            mCategory.setText(task.getCategoryTask());
            this.key = key;
            this.userID = userID;
        }
    }

    class TasksAdapter extends  RecyclerView.Adapter<TaskItemView>{
        private List<Task> mTaskList;
        private List<String> mKeys;
        private String mUserID;


        public TasksAdapter(List<Task> mTaskList, List<String> mKeys, String mUserID) {
            this.mTaskList = mTaskList;
            this.mKeys = mKeys;
            this.mUserID = mUserID;
        }

        @NonNull
        @Override
        public TaskItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TaskItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskItemView holder, int position) {
            holder.bind(mTaskList.get(position),mKeys.get(position), mUserID);
        }

        @Override
        public int getItemCount() {
            return mTaskList.size();
        }
    }
}
