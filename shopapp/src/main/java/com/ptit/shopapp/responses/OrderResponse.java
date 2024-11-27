package com.ptit.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ptit.shopapp.models.Order;
import com.ptit.shopapp.models.OrderDetail;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
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
public class OrderResponse extends BaseResponse {
  private Long id;

  @JsonProperty("user_id")
  private Long userId;

  @JsonProperty("fullname")
  private String fullName;

  @JsonProperty("phone_number")
  private String phoneNumber;

  private String address;

  private String note;

  @JsonProperty("order_date")
  private Date orderDate;

  private String status;

  @JsonProperty("total_money")
  private Float totalMoney;

  @JsonProperty("shipping_method")
  private String shippingMethod;

  @JsonProperty("shipping_address")
  private String shippingAddress;

  @JsonProperty("shipping_date")
  private LocalDate shippingDate;

  @JsonProperty("tracking_number")
  private String trackingNumber;

  @JsonProperty("payment_method")
  private String paymentMethod;

  @JsonProperty("active")
  private Boolean active;

  @JsonProperty("order_details")
  private List<OrderDetail> orderDetails;

  public static OrderResponse fromOrder(Order order) {
    OrderResponse orderResponse =  OrderResponse
        .builder()
        .id(order.getId())
        .userId(order.getUser().getId())
        .fullName(order.getFullName())
        .phoneNumber(order.getPhoneNumber())
        .address(order.getAddress())
        .note(order.getNote())
        .orderDate(order.getOrderDate())
        .status(order.getStatus())
        .totalMoney(order.getTotalMoney())
        .shippingMethod(order.getShippingMethod())
        .shippingAddress(order.getShippingAddress())
        .shippingDate(order.getShippingDate())
        .paymentMethod(order.getPaymentMethod())
        .orderDetails(order.getOrderDetails())
        .build();
    return orderResponse;
  }
}
