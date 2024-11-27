package com.ptit.shopapp.responses;

import com.ptit.shopapp.models.Product;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductListResponse {
  private List<ProductResponse> productResponseList;
  private int totalPages;
}
