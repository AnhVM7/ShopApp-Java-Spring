package com.ptit.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
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
public class ProductImageDTO {

  @JsonProperty("product_id")
  @Min(value = 1, message = "product's id must be > 0")
  private Long productId;

  @JsonProperty("image_url")
  @Size(min = 5, max = 200, message = "image name must be between 3 and 20 characters")
  private String imageUrl;
}
