package com.controller;

import com.dto.AuthorWithBooksDTO;
import com.entity.Author;
import com.service.AuthorService;
import com.service.LibraryService;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Scanner;

@Controller
public class AuthorController {

    private final AuthorService authorService;
    private final LibraryService libraryService;
    private final Scanner scanner = new Scanner(System.in);

    public AuthorController(AuthorService authorService, LibraryService libraryService) {
        this.authorService = authorService;
        this.libraryService = libraryService;
    }

    public void startMainMenu() {
        boolean isRunning = true;
        Scanner scanner = new Scanner(System.in);
        while (isRunning) {
            System.out.print("""
                    
                    === Author Menu ===\s
                    
                    Type "add" to add a record\s
                    Type "list" to list all records\s
                    Type "find" to find a record\s
                    Type "delete" to delete author \s
                    Type "return" to exit return to main menu\s
                    
                    ===========
                    
                    """);
            String input = scanner.nextLine().trim();
            switch (input.toLowerCase()) {
                case "add" -> startNewRecordMenu();
                case "list" -> printAllRecords();
                case "find" -> startFindRecordMenu();
                case "delete" -> startDeleteRecordMenu();
                case "return" -> isRunning = false;
                default -> System.out.printf("Unknown \"%s\" command. Try again.", input);
            }
        }
    }

    private void startNewRecordMenu() {
        boolean isAddingNewRecord = true;
        while (isAddingNewRecord) {
            System.out.print("""
                    
                    === Adding new author record ===\s
                    
                    Please enter lastname and first name of an author
                    split them with at least one space char
                    or type "return" to exit this menu
                    
                    =========================
                    
                    """);
            String input = scanner.nextLine().trim();
            if ("return".equals(input)) {
                isAddingNewRecord = false;
            } else {
                try {
                    Author newAuthor = authorService.buildAuthor(input);
                    startAddBooksMenu(newAuthor);
                    isAddingNewRecord = false;
                } catch (IllegalArgumentException ex) {
                    System.out.println("\n" + ex.getMessage() + "\n");
                } catch (Exception e) {
                    System.out.println("\n An unexpected error occurred. Please try again \n");
                }
            }
        }
    }

    private void printAllRecords() {
        displayList(authorService.getAllRecords());
    }

    private void startFindRecordMenu() {
        boolean isFindingRecord = true;
        while (isFindingRecord) {
            System.out.print("""
                    
                    === Lookup for a record ===\s
                    
                    Please enter id of an author to get author's books
                    or type "return" to exit this menu
                    
                    ===========================
                    
                    """);
            String input = scanner.nextLine().trim();
            if ("return".equals(input)) {
                isFindingRecord = false;
            } else {
                try {
                    AuthorWithBooksDTO authorWithBooksDTO = libraryService.getAuthorBooks(input);
                    System.out.printf("Author %s has written %d books: \n",
                            authorWithBooksDTO.getAuthor().getFullName(), authorWithBooksDTO.getBooks().size());
                    authorWithBooksDTO.getBooks().forEach(book ->
                            System.out.printf("• (id:%d) %s, %s y \n", book.getId(), book.getName(), book.getPublishYear()));
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
                    
                    === Author menu ====
                    
                    Type id of the author you
                    would like to delete.
                    
                    Type "return" to exit this menu.
                    
                    ==========================
                    
                    """);
            String input = scanner.nextLine().trim();
            if ("return".equalsIgnoreCase(input)) {
                isDeletingRecord = false;
            } else {
                try {
                    libraryService.deleteAuthor(input);
                    System.out.println("Author was deleted \n");
                } catch (IllegalArgumentException ex) {
                    System.out.println("\n" + ex.getMessage() + "\n");
                }
            }
        }
    }


    private void displayList(List<Author> authorList) {
        if (authorList.isEmpty()) {
            System.out.println("No authors yet");
        }
        authorList.forEach(author ->
                System.out.println(String.format("(id: %s) %s", author.getId(), author.getFullName())));
    }

    private void startAddBooksMenu(Author newAuthor) {
        boolean isAddingBooks = true;
        while (isAddingBooks) {
            System.out.print("""
                    
                    === Author books menu ====
                    
                    Do you want to add some author books? (y/n)
                    
                    ==========================
                    
                    """);
            String input = scanner.nextLine().trim();
            if ("y".equalsIgnoreCase(input)) {
                boolean isAddingBookList = true;
                while (isAddingBookList) {
                    System.out.print("""
                            
                            === Author books menu ====
                            
                            Enter book name and it's publish date
                            separated by comma.
                            If you want to add several books split
                            records by ';'
                            Example:
                            Book name 1, 2015; Book name 2, 2020
                            
                            Type "return" to exit this menu
                            
                            ==========================
                            
                            """);
                    input = scanner.nextLine().trim();
                    if ("return".equalsIgnoreCase(input.trim())) {
                        isAddingBookList = false;
                        isAddingBooks = false;
                    } else {
                        try {
                            AuthorWithBooksDTO dto = libraryService.createAuthorWithBooks(newAuthor, input);
                            String message = dto.getBooks().isEmpty()
                            ? "Author %s was added".formatted(newAuthor.getFullName())
                            : "Author %s was added along with %d books".formatted(newAuthor.getFirstName(), dto.getBooks().size());
                                System.out.println(message);
                            isAddingBookList = false;
                            isAddingBooks = false;
                        } catch (IllegalArgumentException ex) {
                            System.out.println("\n" + ex.getMessage() + "\n");
                        }
                    }
                }
            } else if ("n".equalsIgnoreCase(input)){
                authorService.createRecord(newAuthor);
                System.out.println(newAuthor.getFullName() + " was added");
                isAddingBooks = false;
            }
        }
    }
}
