package br.com.caroba.tasktracker;

import br.com.caroba.tasktracker.cli.CommandLineInterface;

public class Main {

    public static void main(String[] args) {
        CommandLineInterface cli = new CommandLineInterface();
        cli.showMenu();
    }

}

