package com.mapper;

import com.dto.BookResponse;
import com.dto.BookRequest;
import com.entity.Author;
import com.entity.Book;

public class BookMapper {

    public static Book toModel(BookRequest bookRequest) {
        var book = new Book();
        book.setName(bookRequest.name());
        book.setPublishYear(bookRequest.publishYear());
        book.setAuthor(new Author(bookRequest.authorId()));
        return book;
    }

    public static BookResponse toDTO(Book book) {
        return new BookResponse(
                book.getId(),
                book.getAuthor()!=null? book.getAuthor().getId() : null,
                book.getName(),
                book.getPublishYear(),
                book.getCopiesAmount()
        );
    }

}
