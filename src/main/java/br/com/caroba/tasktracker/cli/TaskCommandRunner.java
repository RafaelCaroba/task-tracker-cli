package br.com.caroba.tasktracker.cli;

import br.com.caroba.tasktracker.model.Status;
import br.com.caroba.tasktracker.model.Task;
import br.com.caroba.tasktracker.service.TaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.util.List;

public class TaskCommandRunner {

    private final TaskService service;

    public TaskCommandRunner(TaskService service) {
        this.service = service;
    }

    public int run(String[] args) {
        if (args.length == 0) {
            printHelp();
            return 1;
        }

        String comando = args[0].toLowerCase();

        try {
            return switch (comando) {
                case "add" -> handleAdd(args);
                case "update" -> handleUpdate(args);
                case "list" -> handleList(args);
                case "delete" -> handleDelete(args);
                case "mark-in-progress" -> handleMarkInProgress(args);
                // handle markDone
                // handle cancelTask (return to pending)?
                default -> handleUnknown(comando);
            };
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            return 1;
        }
    }

    private int handleAdd(String[] args) throws IOException {
        if (args[1] == null || args[1].isBlank()){
            throw new RuntimeException("É necessário passar um argumento contendo a descrição da Task!");
        }

        service.addTask(args[1]);
        System.out.println("Tarefa adicionada com sucesso!");
        return 0;
    }

    private int handleUpdate(String[] args) throws IOException {

        if (args.length < 3) {
            throw new IllegalArgumentException("Uso: task-cli update <id> \\\"description\\\"");
        }

        String taskId, novaDescricao;
        taskId = args[1];
        novaDescricao = args[2];

        validarUpdate(taskId, novaDescricao);
        long id = parseId(taskId);

        service.updateTask(id, novaDescricao);
        System.out.println("Tarefa atualizada com sucesso!");

        return 0;
    }

    private int handleList(String[] args) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        List<Task> taskList;

        if (args.length > 1) {
            switch (args[1].trim().toLowerCase()){
                case "todo":
                    taskList = service.listByStatus(Status.PENDING);
                    break;
                case "in-progress":
                    taskList = service.listByStatus(Status.IN_PROGRESS);
                    break;
                case  "done":
                    taskList = service.listByStatus(Status.COMPLETED);
                    break;
                case "canceled":
                    taskList = service.listByStatus(Status.CANCELED);
                    break;
                default:
                    throw new IllegalArgumentException(
                            "Filtro inválido: " + args[1] + ". Use done, todo ou in-progress"
                    );
            }
        } else {
            taskList = service.listTasks();
        }

        for (Task t : taskList) {
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(t));
        }

        return 0;
    }

    private int handleDelete(String[] args) throws IOException {

        if (args.length != 2 ) {
            throw new IllegalArgumentException("Argumento inválido. " +
                    "Informe apenas o id da tarefa que deseja excluir.");
        }

        long idToDelete = parseId(args[1]);

        service.removeTask(idToDelete);

        System.out.println("Tarefa removida com sucesso!");

        return 0;
    }

    private int handleMarkInProgress(String[] args) throws IOException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Número de argumentos inválido. " +
                    "Informe apenas o id da tarefa que deseja atualizar.");
        }

        long taskId = parseId(args[1]);

        service.startTask(taskId);

        System.out.println("Tarefa iniciada com sucesso!");

        return 0;
    }

    private void validarUpdate(String id, String descricao) {
        if (id.isBlank()) {
            throw new RuntimeException("O id da task deve ser passado como primeiro argumento.");
        }
        if (descricao.isBlank()) {
            throw new IllegalArgumentException("A descrição não pode estar vazia");
        }
    }

    private long parseId(String id){
        try{
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Id inválido: " + id + ". Informe um número inteiro.");
        }
    }

    private int handleUnknown(String comando) {
        System.out.println("Comando desconhecido: " + comando);
        printHelp();
        return 1;
    }



    private void printHelp() {
        System.out.println("""
                Usage:
                task-cli add "description"
                task-cli update <id> "description"
                task-cli delete <id>
                task-cli mark-in-progress <id>
                task-cli mark-done <id>
                task-cli list
                task-cli list done|todo|in-progress
                """);
    }
}
