package com.ptit.shopapp.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ptit.shopapp.responses.ProductResponse;
import java.util.List;
import org.springframework.data.domain.PageRequest;

public interface IProductRedisService {
  void clear();
  List<ProductResponse> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest)
      throws JsonProcessingException;
  void saveAllProducts(List<ProductResponse> productResponses,
      String keyword,
      Long categoryId,
      PageRequest pageRequest) throws JsonProcessingException;
}
