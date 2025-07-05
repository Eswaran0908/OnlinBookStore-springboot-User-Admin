package com.example.demo.ModelRepositoriesFinal;

import com.example.demo.ModelEntitiesFinal.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findByNameContainingOrPriceBetween(String name, double minPrice, double maxPrice, Pageable pageable);

    Page<Book> findByNameContainingOrAuthorNameContainingOrDescriptionContaining(String name, String authorName, String description, Pageable pageable);

    Page<Book> findByNameContainingIgnoreCase(String query, Pageable pageable);

    Page<Book> findByNameContainingIgnoreCaseAndPriceBetweenAndAuthorNameContainingIgnoreCase(String name, double minPrice, double maxPrice, String authorName, Pageable pageable);

    
    @Query("SELECT b FROM Book b WHERE "
            + "(:query IS NULL OR b.name LIKE %:query%) AND "
            + "(:minPrice = 0 OR b.price >= :minPrice) AND "
            + "(:maxPrice = 0 OR b.price <= :maxPrice) AND "
            + "(:authorName IS NULL OR b.authorName LIKE %:authorName%)")
    Page<Book> findBooksByFilters(@Param("query") String query, 
                                  @Param("minPrice") double minPrice, 
                                  @Param("maxPrice") double maxPrice, 
                                  @Param("authorName") String authorName, 
                                  Pageable pageable);

    Optional<Book> findById(Long id);

    @Query("SELECT b FROM Book b WHERE "
            + "(LOWER(b.name) LIKE LOWER(CONCAT('%', :query, '%')) OR :query = '') "
            + "AND (b.price BETWEEN :minPrice AND :maxPrice) "
            + "AND (LOWER(b.authorName) LIKE LOWER(CONCAT('%', :authorName, '%')) OR :authorName = '')")
    List<Book> findBooks(@Param("query") String query,
                         @Param("minPrice") double minPrice,
                         @Param("maxPrice") double maxPrice,
                         @Param("authorName") String authorName);
}
