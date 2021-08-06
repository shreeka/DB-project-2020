package com.example.GrpSB.model;

public class Excercise {
    private String worksheet;
    private String task;

    public Excercise(String worksheet, String task) {
        this.worksheet = worksheet;
        this.task = task;
    }

    public String getWorksheet() {
        return worksheet;
    }

    public void setWorksheet(String worksheet) {
        this.worksheet = worksheet;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
