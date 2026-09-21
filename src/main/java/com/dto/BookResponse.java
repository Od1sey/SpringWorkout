package com.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BookResponse(
        Integer id,
        Integer authorId,
        String name,
        Integer publishYear,
        Integer copies_amount
) {
}
