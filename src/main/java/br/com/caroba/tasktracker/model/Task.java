package br.com.caroba.tasktracker.model;

public class Task {
    
    private final Long id;
    private final String description;
    private Status status;

    public Task(Long id, String description, Status status) {
        this.id = id;
        this.description = description;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void start(){
        if (this.status != Status.PENDING) {
            throw new IllegalArgumentException("Task " + id + " is not pending and cannot be started.");
        }
        this.status = Status.IN_PROGRESS;
    }

    public void complete() {
        if (this.status != Status.IN_PROGRESS) {
            throw new IllegalArgumentException(
                    "Task " + id + "is not in progress and cannot be completed");
        }
        this.status = Status.COMPLETED;
    }

    public void cancel() {
        if (this.status == Status.COMPLETED) {
            throw new IllegalArgumentException(
                    "Task " + id + " is already completed and cannot be canceled");
        }
        this.status = Status.CANCELED;
    }
}
