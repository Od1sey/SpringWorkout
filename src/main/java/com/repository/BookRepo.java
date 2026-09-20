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

    @Query("""
            select b
            from Book b
            join fetch b.author
            where lower(b.name)
            like lower(concat('%', :text, '%'))
            """)
    List<Book> findByNameContaining(@Param("text") String text);

    @Query("""
            select b
            from Book b
            join fetch b.author
            """)
    List<Book> findAllWithAuthors();
}
