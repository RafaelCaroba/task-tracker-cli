package br.com.caroba.tasktracker.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private Task task;

    @BeforeEach
    void setUp() {
        task = Task.newTask(1L, "Teste Task");
    }

    @Test
    void startNaoPendingDeveLancarErro() {
        task.start(); // status -> in progress
        assertThrows(IllegalArgumentException.class, () -> task.start());
    }

    @Test
    void startComPendingNaoDeveLancarErro() {
        assertDoesNotThrow(() -> task.start());
        assertEquals(Status.IN_PROGRESS, task.getStatus());
    }

    @Test
    void completeSemProgressDeveLancarExcecao() {
        assertThrows(IllegalArgumentException.class, () -> task.complete());
    }

    @Test
    void completeComProgressNaoDeveLancarExcecao() {
        task.start();
        assertDoesNotThrow(() -> task.complete());
        assertEquals(Status.COMPLETED, task.getStatus());
    }

    @Test
    void cancelTaskCompletaDeveLancarExcecao() {
        task.start();
        task.complete();
        assertThrows(IllegalArgumentException.class, () -> task.cancel());
    }

    @Test
    void cancelTaskNaoCompletaNaoDeveLancarExcecao() {
        task.start();
        assertDoesNotThrow(() -> task.cancel());
        assertEquals(Status.CANCELED, task.getStatus());
    }
}
