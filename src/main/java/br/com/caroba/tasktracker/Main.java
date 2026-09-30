package br.com.caroba.tasktracker;

import br.com.caroba.tasktracker.cli.TaskCommandRunner;
import br.com.caroba.tasktracker.service.TaskService;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            TaskService taskService = new TaskService();
            TaskCommandRunner runner = new TaskCommandRunner(taskService);
            runner.run(args);
//            CommandLineInterface cli = new CommandLineInterface(taskService);
//            cli.showMenu();
        } catch (IOException e) {
            System.err.println("Erro ao inicializar aplicação: " + e.getMessage());
        }
    }

}

