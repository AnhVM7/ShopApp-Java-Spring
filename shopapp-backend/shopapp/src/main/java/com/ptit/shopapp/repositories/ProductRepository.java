package com.ptit.shopapp.repositories;

import com.ptit.shopapp.models.Product;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
  boolean existsByName(String name);
  Page<Product> findAll(Pageable pageable);
  @Query("SELECT p FROM Product p LEFT JOIN FETCH p.productImages WHERE p.id = :productId")
  Optional<Product> getDetailProduct(@Param("productId") Long productId);
}
