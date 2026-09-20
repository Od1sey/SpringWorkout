package com.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthorResponse(
        String firstName,
        String lastName,
        Integer id
) {
}
