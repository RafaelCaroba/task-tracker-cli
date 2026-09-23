package br.com.caroba.tasktracker.repository;

import br.com.caroba.tasktracker.model.TaskData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class TaskRepository {
    ObjectMapper objectMapper;
    Path path;

    public TaskRepository() {
        this(Path.of("data", "tasks.json"));
    }

    public TaskRepository(Path path) {
        this.path = path;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public TaskData loadData() throws IOException {
        verificarExistenciaFile();

        String json = Files.readString(path);

        if (json.isBlank()){
            TaskData dataVazio = new TaskData(1L, new ArrayList<>());
            save(dataVazio);
            return dataVazio;
        }

        return objectMapper.readValue(json, TaskData.class);
    }

    public void save(TaskData data) throws IOException {
        String json = objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(data);

        Files.writeString(path, json);
    }

    private void verificarExistenciaFile() throws IOException {
        Path parent = path.getParent();

        if (parent != null && Files.notExists(parent)) {
            Files.createDirectories(parent);
        }

        if (Files.notExists(path)) {
            TaskData dataVazio = new TaskData(1L, new ArrayList<>());
            save(dataVazio);
        }
    }

}
