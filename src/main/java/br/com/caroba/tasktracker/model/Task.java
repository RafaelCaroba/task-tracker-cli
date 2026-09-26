package br.com.caroba.tasktracker.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class Task {

    private Long id;
    private String description;
    private Status status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime lastUpdatedAt;

    public Task() {
    }

    public Task(Long id, String description, Status status, LocalDateTime createdAt, LocalDateTime lastUpdatedAt) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    // Factory para criar nova tarefa
    public static Task newTask(Long id, String description) {
        LocalDateTime now = LocalDateTime.now();
        return new Task(id, description, Status.PENDING, now, now);
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

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void start(){
        if (this.status != Status.PENDING) {
            throw new IllegalArgumentException("Task " + id + " is not pending and cannot be started.");
        }
        this.status = Status.IN_PROGRESS;
        atualizaHoraUpdate();
    }

    public void complete() {
        if (this.status != Status.IN_PROGRESS) {
            throw new IllegalArgumentException(
                    "Task " + id + "is not in progress and cannot be completed");
        }
        this.status = Status.COMPLETED;
        atualizaHoraUpdate();
    }

    public void cancel() {
        if (this.status == Status.COMPLETED) {
            throw new IllegalArgumentException(
                    "Task " + id + " is already completed and cannot be canceled");
        }
        this.status = Status.CANCELED;
        atualizaHoraUpdate();
    }

    private void atualizaHoraUpdate() {
        this.lastUpdatedAt = LocalDateTime.now();
    }
}
