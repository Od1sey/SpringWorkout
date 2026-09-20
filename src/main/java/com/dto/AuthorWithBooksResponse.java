package com.dto;

import java.util.List;

public record AuthorWithBooksResponse(
        String firstName,
        String lastName,
        Integer id,
        List<BookResponse> books
) {
}
