package com.ptit.shopapp.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ptit.shopapp.responses.ProductResponse;
import com.ptit.shopapp.services.IProductRedisService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductRedisService implements IProductRedisService {

  @Override
  public void clear() {

  }

  @Override
  public List<ProductResponse> getAllProducts(String keyword, Long categoryId,
      PageRequest pageRequest) throws JsonProcessingException {
    return null;
  }

  @Override
  public void saveAllProducts(List<ProductResponse> productResponses, String keyword,
      Long categoryId, PageRequest pageRequest) throws JsonProcessingException {

  }
}
