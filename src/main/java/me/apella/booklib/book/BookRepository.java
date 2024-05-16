package me.apella.booklib.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {
    @Query("""
            SELECT book
            FROM Book book
            WHERE book.archived = false
            AND book.shareable = true
            AND book.owner.id != :userId
            """)
    Page<Book> findAllDisplayableBooks(Pageable pageable, Integer userId);

    // how does this differ with the book specification implementation
    @Query("""
            SELECT book
            FROM Book book
            WHERE book.archived = true
            AND book.shareable = true
            AND book.owner.id = :userId
            """)
    Page<Book> findAllBooksByOwner(Pageable pageable, Integer id);
}
