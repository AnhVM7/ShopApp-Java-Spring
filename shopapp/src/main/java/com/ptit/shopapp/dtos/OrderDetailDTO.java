package com.ptit.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
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
public class OrderDetailDTO {
  @JsonProperty("order_id")
  @Min(value = 1, message = "order's id must be > 0")
  private Long orderId;

  @JsonProperty("product_id")
  @Min(value = 1, message = "product's id must be > 0")
  private Long productId;

  @Min(value = 0, message = "price must be >= 0")
  private Float price;

  @JsonProperty("number_of_product")
  @Min(value = 1, message = "number_of_product must be >= 1")
  private int numberOfProduct;

  @Min(value = 0, message = "total_money must be >= 0")
  @JsonProperty("total_money")
  private Float totalMoney;

  private String color;
}
