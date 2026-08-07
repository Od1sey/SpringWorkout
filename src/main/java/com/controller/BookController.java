package com.controller;

import com.entity.Book;
import com.service.BookService;
import com.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Scanner;

@Controller
public class BookController {

    private final BookService bookService;
    private final LibraryService libraryService;
    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    public BookController(BookService bookService, LibraryService libraryService) {
        this.bookService = bookService;
        this.libraryService = libraryService;
    }

    public void startMainMenu() {
        boolean isRunning = true;
        while (isRunning) {
            System.out.print("""
                    
                    === Book menu ===\s
                    
                    Type "add" to add a record\s
                    Type "list" to list all records\s
                    Type "find" to find a record\s
                    Type "move" to move record from one author to another \s
                    Type "delete" to delete the record \s
                    Type "return" to return to main menu\s
                    
                    ===========
                    
                    """);
            String input = scanner.nextLine().trim();
            switch (input.toLowerCase()) {
                case "add" -> startNewRecordMenu();
                case "list" -> listAllRecords();
                case "find" -> startFindRecordMenu();
                case "move" -> startMoveRecordMenu();
                case "delete" -> startDeleteRecordMenu();
                case "return" -> isRunning = false;
                default -> System.out.println("Unknown command. Try again.");
            }
        }
    }

    private void startNewRecordMenu() {
        boolean isAddingNewRecord = true;
        while (isAddingNewRecord) {
            System.out.print("""
                    
                    === Adding a book record ===\s
                    
                    Please enter name of a book,
                    it's publish year and author id below.
                    Separate values with comma.
                    
                    Example: Book Name, 2020, 2
                    
                    Type "return" to exit this menu
                    
                    =========================
                    
                    """);
            String input = scanner.nextLine().trim();
            if ("return".equals(input)) {
                isAddingNewRecord = false;
            } else {
                try {
                    Book book = bookService.buildBook(input);
                    bookService.createRecord(book);
                    System.out.printf("Book with name %s was added \n", book.getName());
                    isAddingNewRecord = false;
                } catch (IllegalArgumentException ex) {
                    System.out.println("\n" + ex.getMessage() + "\n");
                }
            }
        }
    }

    private void listAllRecords() {
        displayList(bookService.getAllRecords());
    }

    private void startFindRecordMenu() {
        boolean isFindingRecord = true;
        while (isFindingRecord) {
            System.out.print("""
                    
                    === Lookup for a book record ===\s
                    
                    Please enter name of a book and below
                    or "return" to exit this menu
                    
                    ===========================
                    
                    """);
            String input = scanner.nextLine().trim();
            if ("return".equals(input)) {
                isFindingRecord = false;
            } else {
                try {
                    displayList(bookService.getRecordsByQuery(input));
                } catch (IllegalArgumentException ex) {
                    System.out.println("\n" + ex.getMessage() + "\n");
                }
            }
        }
    }

    private void startMoveRecordMenu() {
        boolean isMovingRecord = true;
        while (isMovingRecord) {
            System.out.print("""
                    
                    === Moving book record ===\s
                    
                    Please enter the book ID and the ID of the author
                    to whom the book should be assigned.
                    Separate the values with a comma.
                    
                    Example:
                    1, 5
                    This will assign the book with ID 1 to the author with ID 5.
                    
                    Type "return" to exit this menu
                    
                    ===========================
                    
                    """);
            String input = scanner.nextLine().trim();
            if ("return".equalsIgnoreCase(input)) {
                isMovingRecord = false;
            } else {
                try {
                    libraryService.updateBookAuthor(input);
                } catch (IllegalArgumentException ex) {
                    System.out.println("\n" + ex.getMessage() + "\n");
                }
            }
        }
    }

    private void startDeleteRecordMenu() {
        boolean isDeletingRecord = true;
        while (isDeletingRecord) {
            System.out.print("""
                    
                    === Books menu ====
                    
                    Type id of the book you
                    would like to delete.
                    
                    Type "return" to exit this menu.
                    
                    ==========================
                    
                    """);
            String input = scanner.nextLine().trim();
            if ("return".equalsIgnoreCase(input)) {
                isDeletingRecord = false;
            } else {
                try {
                    bookService.deleteRecord(input);
                    System.out.println("Book with id %s was deleted \n".formatted(input));
                    isDeletingRecord = false;
                } catch (IllegalArgumentException ex) {
                    System.out.println("\n" + ex.getMessage() + "\n");
                }
            }
        }
    }

    private void displayList(List<Book> bookList) {
        if (bookList.isEmpty()) {
            System.out.println("List is empty");
        } else {
            System.out.println();
            bookList.forEach(book ->
                    System.out.printf("• (id: %d) %s (Год публикации: %d) \n", book.getId(), book.getName(), book.getPublishYear()));
        }
    }
}
