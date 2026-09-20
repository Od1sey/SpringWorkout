package com.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BookRequest(
        @NotBlank
        @Size(min = 2)
        String name,
        Integer publishYear,
        @NotBlank
        Integer authorId
) {
    public boolean isEmpty() {
        return name == null &&
                publishYear == null &&
                authorId == null;
    }
}
