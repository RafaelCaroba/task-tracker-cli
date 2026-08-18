package br.com.caroba.tasktracker.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import br.com.caroba.tasktracker.dto.TaskDTO;
import br.com.caroba.tasktracker.exception.TaskNotFoundException;
import br.com.caroba.tasktracker.model.Status;
import br.com.caroba.tasktracker.model.Task;

public class TaskService {

    private final List<Task> tasks = new ArrayList<>();
    private long nextId = 1;

    public Task addTask(String description) {
        Task task = new Task(nextId++, description, Status.PENDING);
        tasks.add(task);
        return task;
    }

    public List<TaskDTO> listTasks(){

        return tasks.stream()
                .map(task -> new TaskDTO(
                        task.getId(),
                        task.getDescription(),
                        task.getStatus()
                ))
                .toList();
    }

    public Task startTask(long id) {
        Task task = findTaskById(id);
        task.start();
        return task;
    }

    public Task completeTask(long id) {
        Task task = findTaskById(id);
        task.complete();
        return task;
    }

    public Task cancelTask(long id) {
        Task task = findTaskById(id);
        task.cancel();
        return task;
    }

    public void removeTask(long id) {
        if (!tasks.removeIf(task -> task.getId() == id)) {
            throw new TaskNotFoundException("Task with id " + id + " not found.");
        }
    }

    private Task findTaskById(long id) {
        return tasks.stream()
                .filter(task -> task.getId() == id)
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found."));
    }
}
