package com.ptit.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ptit.shopapp.models.OrderDetail;
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
public class OrderDetailResponse {
  private Long id;

  @JsonProperty("order_id")
  private Long orderId;

  @JsonProperty("product_id")
  private Long productId;

  private Float price;

  @JsonProperty("number_of_product")
  private int numberOfProduct;

  @JsonProperty("total_money")
  private Float totalMoney;

  private String color;

  public static OrderDetailResponse fromOrderDetail(OrderDetail orderDetail){
    return OrderDetailResponse
        .builder()
        .id(orderDetail.getId())
        .orderId(orderDetail.getOrder().getId())
        .productId(orderDetail.getProduct().getId())
        .price(orderDetail.getPrice())
        .numberOfProduct(orderDetail.getNumberOfProduct())
        .totalMoney(orderDetail.getTotalMoney())
        .color(orderDetail.getColor())
        .build();
  }
}
