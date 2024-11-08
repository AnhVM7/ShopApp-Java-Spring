package com.ptit.shopapp.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.cglib.core.Local;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "fullname", length = 100)
  private String fullName;

  @Column(name = "email", length = 100)
  private String email;

  @Column(name = "phone_number", nullable = false, length = 20)
  private String phoneNumber;

  @Column(name = "address", nullable = false, length = 200)
  private String address;

  @Column(length = 100)
  private String note;

  @Column(name = "order_date")
  private Date orderDate;

  private String status;

  @Column(name = "total_money")
  private Float totalMoney;

  @Column(name = "shipping_method")
  private String shippingMethod;

  @Column(name = "shipping_address")
  private String shippingAddress;

  @Column(name = "shipping_date")
  private LocalDate shippingDate;

  @Column(name = "tracking_number")
  private String trackingNumber;

  @Column(name = "payment_method")
  private String paymentMethod;

  private Boolean active; // thuoc ve admin
}
