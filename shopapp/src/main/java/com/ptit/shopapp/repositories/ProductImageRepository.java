package com.ptit.shopapp.repositories;

import com.ptit.shopapp.models.ProductImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
  List<ProductImage> findByProductId(Long productId);
}
