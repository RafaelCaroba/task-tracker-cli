package br.com.caroba.tasktracker.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.caroba.tasktracker.dto.TaskDTO;
import br.com.caroba.tasktracker.exception.TaskNotFoundException;
import br.com.caroba.tasktracker.model.Status;
import br.com.caroba.tasktracker.model.Task;
import br.com.caroba.tasktracker.model.TaskData;
import br.com.caroba.tasktracker.repository.TaskRepository;

public class TaskService {

    private final TaskRepository repository;
    private final List<Task> tasks;
    private long nextId;

    public TaskService() throws IOException {
        this(new TaskRepository());
    }

    public TaskService(TaskRepository repository) throws IOException {
        this.repository = repository;
        TaskData data = repository.loadData();
        this.tasks = new ArrayList<>(data.getTasks());
        this.nextId = data.getNextId();
    }

    public Task addTask(String description) throws IOException {
        Task task = Task.newTask(nextId++, description);
        tasks.add(task);
        save();
        return task;
    }

    public List<Task> listTasks(){
        return new ArrayList<>(tasks);
    }

    public List<TaskDTO> listarTasksParaIniciar() {
        return tasks.stream()
                .filter(task -> task.getStatus() == Status.PENDING)
                .map(task -> new TaskDTO(
                        task.getId(),
                        task.getDescription(),
                        task.getStatus()
                ))
                .toList();
    }

    public List<TaskDTO> listarTasksParaCompletar() {
        return tasks.stream()
                .filter(task -> task.getStatus() == Status.IN_PROGRESS)
                .map(task -> new TaskDTO(
                        task.getId(),
                        task.getDescription(),
                        task.getStatus()
                ))
                .toList();
    }

    public List<TaskDTO> listarTasksParaCancelar() {
        return tasks.stream()
                .filter(task -> task.getStatus() != Status.COMPLETED)
                .map(task -> new TaskDTO(
                        task.getId(),
                        task.getDescription(),
                        task.getStatus()
                ))
                .toList();
    }

    public Task startTask(long id) throws IOException {
        Task task = findTaskById(id);
        task.start();
        save();
        return task;
    }

    public Task completeTask(long id) throws IOException {
        Task task = findTaskById(id);
        task.complete();
        save();
        return task;
    }

    public Task cancelTask(long id) throws IOException {
        Task task = findTaskById(id);
        task.cancel();
        save();
        return task;
    }

    public void removeTask(long id) throws IOException {
        if (!tasks.removeIf(task -> task.getId() == id)) {
            throw new TaskNotFoundException("Task with id " + id + " not found.");
        }
        save();
    }

    private Task findTaskById(long id) {
        return tasks.stream()
                .filter(task -> task.getId() == id)
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found."));
    }

    private void save() throws IOException {
        TaskData data = new TaskData(nextId, tasks);
        repository.save(data);
    }
}
