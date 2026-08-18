package br.com.caroba.tasktracker.dto;

import br.com.caroba.tasktracker.model.Status;

public record TaskDTO(
        Long id,
        String description,
        Status status
) {
}
