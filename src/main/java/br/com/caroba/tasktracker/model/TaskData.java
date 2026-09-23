package br.com.caroba.tasktracker.model;

import java.util.List;

public class TaskData {

    private long nextId;
    private List<Task> tasks;

    public TaskData(){
    }

    public TaskData(long nextId, List<Task> tasks) {
        this.nextId = nextId;
        this.tasks = tasks;
    }

    public long getNextId() {
        return nextId;
    }

    public void setNextId(long nextId) {
        this.nextId = nextId;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
