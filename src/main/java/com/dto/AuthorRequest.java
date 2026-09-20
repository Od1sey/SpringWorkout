package com.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record AuthorRequest(
        @NotBlank
        @Valid
        @Size(min=2, message = "Last name length should be at least 2 characters")
        String lastName,
        @NotBlank
        @Size(min=2, message = "First name length should be at least 2 characters")
        String firstName,
        List<BookRequest> books
) {
}
