package com.ptit.shopapp.services;

import com.ptit.shopapp.dtos.ProductDTO;
import com.ptit.shopapp.dtos.ProductImageDTO;
import com.ptit.shopapp.exceptions.DataNotFoundException;
import com.ptit.shopapp.exceptions.InvalidParamException;
import com.ptit.shopapp.models.Product;
import com.ptit.shopapp.models.ProductImage;
import com.ptit.shopapp.responses.ProductResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductService {
  Product createProduct(ProductDTO productDTO) throws Exception;
  Product getProductById(long id) throws Exception;

  Page<ProductResponse> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest);

  Product updateProduct(long id, ProductDTO productDTO) throws Exception;
  void deleteProduct(long id);
  boolean existsByName(String name);

  public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO)
      throws Exception;

  List<Product> findProductsByIds(List<Long> productIds);
}
