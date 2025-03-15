package com.madproject.tutortracker;
public class ScheduleItem {
    private String task;
    private String date;
    private String from;
    private String to;

    public ScheduleItem(String task, String date, String from, String to) {
        this.task = task;
        this.date = date;
        this.from = from;
        this.to = to;
    }

    public String getTask() {
        return task;
    }

    public String getDate() {
        return date;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
