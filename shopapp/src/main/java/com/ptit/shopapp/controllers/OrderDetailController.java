package com.ptit.shopapp.controllers;

import com.ptit.shopapp.components.LocalizationUtil;
import com.ptit.shopapp.dtos.OrderDetailDTO;
import com.ptit.shopapp.exceptions.DataNotFoundException;
import com.ptit.shopapp.models.OrderDetail;
import com.ptit.shopapp.responses.OrderDetailResponse;
import com.ptit.shopapp.services.IOrderDetailService;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {
  private final IOrderDetailService orderDetailService;
  private final LocalizationUtil localizationUtil;
  //them moi 1 order detail
  @PostMapping
  public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO,
      BindingResult result) {
    try {
      OrderDetail orderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
      return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail));
    } catch (DataNotFoundException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }

  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getOrderDetailById(@Valid @PathVariable Long id){
    try {
      OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
      return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail));
    } catch (DataNotFoundException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }

  }

  @GetMapping("/order/{order_id}")
  public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("order_id") Long orderId){
    List<OrderDetail> orderDetailList = orderDetailService.findByOrderId(orderId);
    List<OrderDetailResponse> list = orderDetailList
        .stream()
        .map(OrderDetailResponse::fromOrderDetail)
        .toList();
    return ResponseEntity.ok(list);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable Long id,
      @RequestBody OrderDetailDTO orderDetailDTO){
    try {
      OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, orderDetailDTO);
      return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail));
    } catch (DataNotFoundException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable Long id){
    orderDetailService.deleteOrderDetail(id);
    return ResponseEntity.ok("delete order detail with id = " + id + " successfully");
  }
}
