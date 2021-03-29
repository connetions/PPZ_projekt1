package com.example.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerView_Config {
    private Context mContext;
    private TasksAdapter mTaskAdapter;
    public void setConfig(RecyclerView recyclerView, Context context, List<Task> tasks, List<String> keys){
        mContext = context;
        mTaskAdapter = new TasksAdapter(tasks, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mTaskAdapter);
    }


    class TaskItemView extends RecyclerView.ViewHolder{
        private TextView mTitle, mTime, mDate, mCategory;

        private String key;

        public TaskItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext).inflate(R.layout.task_list_item, parent, false));

            mTitle = itemView.findViewById(R.id.taskTitle);
            mTime = itemView.findViewById(R.id.taskTime);
            mDate = itemView.findViewById(R.id.taskDate);
            mCategory = itemView.findViewById(R.id.taskCategory);
        }

        public void bind(Task task, String key) {
            mTitle.setText(task.getTitleTask());
            mTime.setText(task.getTimeTask());
            mDate.setText(task.getDateTask());
            mCategory.setText(task.getCategoryTask());
            this.key = key;
        }
    }

    class TasksAdapter extends  RecyclerView.Adapter<TaskItemView>{
        private List<Task> mTaskList;
        private List<String> mKeys;

        public TasksAdapter(List<Task> mTaskList, List<String> mKeys) {
            this.mTaskList = mTaskList;
            this.mKeys = mKeys;
        }

        @NonNull
        @Override
        public TaskItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TaskItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskItemView holder, int position) {
            holder.bind(mTaskList.get(position),mKeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mTaskList.size();
        }
    }
}
