package com.repository;

import com.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepo extends JpaRepository<Book, Integer> {

    List<Book> findByAuthorId(int id);
    List<Book> findByNameContaining(String text);

    @Query("""
        SELECT b FROM Book b
        WHERE LOWER(b.author.fullName)
        LIKE LOWER(CONCAT('%', :name, '%'))
        """)
    List<Book> findByAuthorFullName(@Param("name") String name);
}
