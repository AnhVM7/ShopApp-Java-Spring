package com.ptit.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
  @JsonProperty("product_id")
  private Long productId;

  @JsonProperty("quantity")
  private Integer quantity;
}
