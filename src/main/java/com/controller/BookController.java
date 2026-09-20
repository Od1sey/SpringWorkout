package com.controller;

import com.dto.BookRequest;
import com.dto.BookResponse;
import com.mapper.BookMapper;
import com.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/book")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<BookResponse>> getAll() {
        List<BookResponse> books = bookService.getAllRecords().stream().map(BookMapper::toDTO).toList();
        return ResponseEntity.ok().body(books);
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getByQuery(@RequestParam("query") String query) {
        List<BookResponse> books = bookService.getRecordsByQuery(query).stream().map(BookMapper::toDTO).toList();
        return ResponseEntity.ok().body(books);
    }

    @PostMapping
    public ResponseEntity<BookResponse> create(@Valid @RequestBody BookRequest createBookRequest) {
        var createdBook = bookService.createRecord(BookMapper.toModel(createBookRequest));
        var bookResponse = BookMapper.toDTO(createdBook);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookResponse);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<BookResponse> updateRecord(
            @PathVariable("id") int id,
            @Valid @RequestBody BookRequest updateBookRequest) {
        var book = bookService.updateRecord(id, BookMapper.toModel(updateBookRequest));
        var bookResponse = BookMapper.toDTO(book);
        return ResponseEntity.ok().body(bookResponse);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        bookService.deleteRecord(id);
        return ResponseEntity.ok().build();
    }
}
