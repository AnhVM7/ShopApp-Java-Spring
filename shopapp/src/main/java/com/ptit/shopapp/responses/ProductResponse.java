package com.ptit.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ptit.shopapp.models.Product;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductResponse extends BaseResponse{
  private String name;

  private Float price;

  private String thumbnail;
  private String description;

  @JsonProperty("category_id")
  private Long categoryId;

  public static ProductResponse fromProduct(Product product){
    ProductResponse productResponse =  ProductResponse
        .builder()
        .name(product.getName())
        .price(product.getPrice())
        .thumbnail(product.getThumbnail())
        .description(product.getDescription())
        .categoryId(product.getCategory().getId())
        .build();
    productResponse.setCreatedAt(product.getCreatedAt());
    productResponse.setUpdatedAt(product.getUpdatedAt());
    return productResponse;
  }
}
