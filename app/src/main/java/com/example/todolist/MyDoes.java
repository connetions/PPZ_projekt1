package com.example.todolist;

public class MyDoes {

    String titleTask, dateTask, timeTask, keyTask,userID,categoryTask,lastCategoryTask;

    public MyDoes() {
    }

    public MyDoes(String titleTask, String dateTask, String timeTask, String keyTask, String userID,String categoryTask, String lastCategoryTask) {
        this.titleTask = titleTask;
        this.dateTask = dateTask;
        this.timeTask = timeTask;
        this.keyTask = keyTask;
        this.userID = userID;
        this.categoryTask = categoryTask;
        this.lastCategoryTask = lastCategoryTask;
    }

    public String getCategoryTask() {
        return categoryTask;
    }

    public void setCategoryTask(String categoryTask) {
        this.categoryTask = categoryTask;
    }

    public String getTitleTask() {
        return titleTask;
    }

    public void setTitleTask(String titleTask) {
        this.titleTask = titleTask;
    }

    public String getDateTask() {
        return dateTask;
    }

    public void setDateTask(String dateTask) {
        this.dateTask = dateTask;
    }

    public String getTimeTask() {
        return timeTask;
    }

    public void setTimeTask(String timeTask) {
        this.timeTask = timeTask;
    }

    public String getKeyTask() {
        return keyTask;
    }

    public void setKeyTask(String keyTask) {
        this.keyTask = keyTask;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getLastCategoryTask() {
        return lastCategoryTask;
    }

    public void setLastCategoryTask(String lastCategoryTask) {
        this.lastCategoryTask = lastCategoryTask;
    }
}
