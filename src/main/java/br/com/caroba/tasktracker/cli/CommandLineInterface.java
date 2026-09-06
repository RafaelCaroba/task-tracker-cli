package br.com.caroba.tasktracker.cli;

import br.com.caroba.tasktracker.dto.TaskDTO;
import br.com.caroba.tasktracker.service.TaskService;

import java.util.List;
import java.util.Scanner;

public class CommandLineInterface {

    private TaskService taskService = new TaskService();

    private Scanner scanner = new Scanner(System.in);

    private static final String MENU = """
            =================================
                      TASK TRACKER
            =================================
            1. List tasks
            2. Add task
            3. Start task
            4. Complete task
            5. Cancel task
            6. Remove task
            0. Exit
            """;

    public void showMenu() {
        int opcao = -1;
        while(opcao != 0) {
            System.out.println(MENU);
            opcao = lerRequiredOpcao();
            System.out.println();

            switch (opcao) {
                case 1: listTasks();
                    break;

                case 2: addTask();
                    break;

                case 3: startTask();
                    break;

                case 4: completeTask();
                    break;

                case 5: cancelTask();
                    break;
            }
        }
        scanner.close();

    }

    private void listTasks() {
        System.out.println("LISTA DE TAREFAS: \n");
        List<TaskDTO> lista = taskService.listTasks();

        if (lista.isEmpty()) {
            System.out.println("Nenhuma tarefa cadastrada.\n");
            return;
        }

        for (TaskDTO task : lista) {
            System.out.printf("[%d] - %s - %s%n", task.id(), task.description(), task.status());
        }
        System.out.println();
    }

    private void addTask() {
        System.out.println("ADICIONAR UMA TAREFA: ");
        String descricao = lerRequiredDescricao();
        taskService.addTask(descricao);
        System.out.println("Tarefa adicionada com sucesso! \n");
    }

    private void startTask() {
        System.out.println("SELECIONE UMA TAREFA PARA INICIAR: ");
        List<TaskDTO> taskList = taskService.listarTasksParaIniciar();

        if (taskList.isEmpty()) {
            System.out.println("Nenhuma tarefa disponível.");
            return;
        }

        exibirTarefas(taskList);
        int opcaoId = lerRequiredOpcao();

        taskService.startTask(opcaoId);
        System.out.println("Tarefa iniciada com sucesso! \n");

    }

    private void completeTask() {
        System.out.println("SELECIONE UMA TAREFA PARA COMPLETAR: ");
        List<TaskDTO> taskList = taskService.listarTasksParaCompletar();

        if (taskList.isEmpty()) {
            System.out.println("Nenhuma tarefa disponível");
            return;
        }

        exibirTarefas(taskList);
        int opcaoId = lerRequiredOpcao();

        taskService.completeTask(opcaoId);
        System.out.println("Tarefa completa com sucesso! \n");
    }

    private void cancelTask(){
        System.out.println("SELECIONE UMA TASK PARA CANCELAR: ");
        List<TaskDTO> taskList = taskService.listarTasksParaCancelar();

        if (taskList.isEmpty()) {
            System.out.println("Nenhuma tarefa disponível");
            return;
        }

        exibirTarefas(taskList);
        int opcaoId = lerRequiredOpcao();
        taskService.cancelTask(opcaoId);
        System.out.println("Tarefa cancelada com sucesso!");

    }

    private int lerRequiredOpcao() {
        while (true){
            System.out.print("Escolha uma opção: ");
            String opcao = scanner.nextLine();

            if (opcao == null || opcao.isBlank()) {
                System.out.println("Digite um número.");
            }

            try {
                return Integer.parseInt(opcao);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite apenas números");
            }
        }
    }

    private String lerRequiredDescricao() {
        while (true) {
            System.out.print("Descrição: ");
            String descricao = scanner.nextLine();

            if (descricao != null && !descricao.isBlank()) {
                return descricao.trim();
            }

            System.out.println("Descrição inválida. Digite novamente.");
        }
    }

    private void exibirTarefas(List<TaskDTO> lista) {

        for (TaskDTO task : lista) {
            System.out.printf("[%d] - %s - %s%n", task.id(), task.description(), task.status());
        }
    }
}
