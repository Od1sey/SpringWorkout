package com.mapper;

import com.dto.AuthorRequest;
import com.dto.AuthorResponse;
import com.dto.AuthorWithBooks;
import com.dto.AuthorWithBooksResponse;
import com.entity.Author;

public class AuthorMapper {

    public static Author toModel(AuthorRequest authorRequest) {
        Author author = new Author(
                authorRequest.lastName(),
                authorRequest.firstName());

        if (authorRequest.books() != null) {
            authorRequest.books()
                    .stream()
                    .map(BookMapper::toModel)
                    .forEach(author::addBook);
        }
        return author;
    }

    public static AuthorResponse toAuthorDTO(Author author) {
        return new AuthorResponse(
                author.getFirstName(),
                author.getLastName(),
                author.getId()
        );
    }

    public static AuthorWithBooksResponse toAuthorWithBooksDTO(Author author){
        return new AuthorWithBooksResponse(
                author.getFirstName(),
                author.getLastName(),
                author.getId(),
                author.getBooks().stream().map(BookMapper::toDTO).toList()
        );
    }

}
