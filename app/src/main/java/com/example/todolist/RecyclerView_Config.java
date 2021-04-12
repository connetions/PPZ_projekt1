package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
        private String userID;
        private String title;
        private String date;
        private String time;
        private String category;

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
            
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Task task = new Task();
                    task.setTitleTask(title);
                    task.setDateTask(date);
                    task.setTimeTask(time);
                    task.setCategoryTask("Ukonczone");
                    task.setUserID(userID);
                    task.setKeyTask(key);
                    if (!category.equals("Ukonczone")){
                        new FirebaseDatabaseHelper("Ukonczone").addTask(task, new FirebaseDatabaseHelper.DataStatus() {
                            @Override
                            public void DataIsLoaded(List<Task> tasks, List<String> keys) {

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

                        new FirebaseDatabaseHelper(category).deleteTask(key, new FirebaseDatabaseHelper.DataStatus() {
                            @Override
                            public void DataIsLoaded(List<Task> tasks, List<String> keys) {

                            }

                            @Override
                            public void DataIsInserted() {

                            }

                            @Override
                            public void DataIsUpdated() {

                            }

                            @Override
                            public void DataIsDeleted() {
                                Toast.makeText(mContext, "Finish", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


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
            this.title = task.getTitleTask();
            this.date = task.getDateTask();
            this.time = task.getTimeTask();
            this.category = task.getCategoryTask();
            if(data_checker(task.getTimeTask(), task.getDateTask())){
                mTitle.setTextColor(0xFF9F1C13);
                mTime.setTextColor(0xFF9F1C13);
                mDate.setTextColor(0xFF9F1C13);
                mCategory.setTextColor(0xFF9F1C13);
            }
        }





        boolean data_checker(String time, String day) {
            int cnt = 0;
            Calendar now = Calendar.getInstance();
            int currentHour = now.get(Calendar.HOUR_OF_DAY);
            int currentMinute = now.get(Calendar.MINUTE);
            int currentSecond = now.get(Calendar.SECOND);
            int currentDay = now.get(Calendar.DAY_OF_MONTH);
            int currentMonth = now.get(Calendar.MONTH);
            int currentYear = now.get(Calendar.YEAR);



            SimpleDateFormat format2 = new SimpleDateFormat("MM:dd:yyyy");

            String currentDayy = Integer.toString(currentMonth + 1) + ":" + Integer.toString(currentDay ) + ":" + Integer.toString(currentYear);


            try {

                Date taskDay = format2.parse(day);
                Date cday = format2.parse(currentDayy);
                if (taskDay.getTime() <= cday.getTime()) {

                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    String currentTime = Integer.toString(currentHour) + ":" + Integer.toString(currentMinute) + ":" + Integer.toString(currentSecond);
                    try {
                        Date taskTime = format.parse(time + ":00");
                        Date ctime = format.parse(currentTime);
                        if (taskTime.getTime() < ctime.getTime()) {
                            return true;
                        }

                    } catch (Exception e) {

                    }

                }


            } catch (Exception e) {

            }



            return false;
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
