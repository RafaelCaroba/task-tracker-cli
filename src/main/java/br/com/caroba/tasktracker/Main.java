package br.com.caroba.tasktracker;

import br.com.caroba.tasktracker.cli.CommandLineInterface;
import br.com.caroba.tasktracker.service.TaskService;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            TaskService taskService = new TaskService();
            CommandLineInterface cli = new CommandLineInterface(taskService);
            cli.showMenu();
        } catch (IOException e) {
            System.err.println("Erro ao inicializar aplicação: " + e.getMessage());
        }
    }

}

