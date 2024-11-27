package com.ptit.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
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
public class OrderDTO {
  @JsonProperty("user_id")
  @Min(value = 1, message = "user's id must be greater than 0")
  private Long userId;

  @JsonProperty("fullname")
  private String fullName;

  private String email;

  @JsonProperty("phone_number")
  @NotBlank(message = "Phone number is required")
  @Size(min = 5, message = "phone number must be at least 5 characters")
  private String phoneNumber;

  private String address;

  private String note;

  @JsonProperty("total_money")
  @Min(value = 0, message = "total money must be greater than or equal to 0")
  private Float totalMoney;

  @JsonProperty("shipping_method")
  private String shippingMethod;

  @JsonProperty("shipping_address")
  private String shippingAddress;

  @JsonProperty("shipping_date")
  private LocalDate shippingDate;

  @JsonProperty("payment_method")
  private String paymentMethod;

  @JsonProperty("cart_items")
  private List<CartItemDTO> cartItems;
}