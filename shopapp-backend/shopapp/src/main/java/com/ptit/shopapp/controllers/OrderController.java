package com.ptit.shopapp.controllers;

import com.ptit.shopapp.dtos.OrderDTO;
import com.ptit.shopapp.exceptions.DataNotFoundException;
import com.ptit.shopapp.models.Order;
import com.ptit.shopapp.responses.OrderResponse;
import com.ptit.shopapp.services.IOrderService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
  private final IOrderService orderService;
  @PostMapping("")
  public ResponseEntity<?> createOrder(@RequestBody @Valid OrderDTO orderDTO, BindingResult result){
    try {
      if(result.hasErrors()){
        List<String> errorMessages = result.getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .toList();
        return ResponseEntity.badRequest().body(errorMessages);
      }
      OrderResponse orderResponse = orderService.createOrder(orderDTO);
      return ResponseEntity.ok(orderResponse);
    } catch (Exception e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/user/{user_id}")
  public ResponseEntity<?> getOrders(@Valid @PathVariable("user_id") Long userId){
    try{
      List<OrderResponse> orderResponseList = orderService.findByUserId(userId);
      return ResponseEntity.ok(orderResponseList);
    } catch(Exception e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getOrder(@Valid @PathVariable("id") Long id){
    try{
      OrderResponse orderResponse = orderService.getOrderById(id);
      return ResponseEntity.ok(orderResponse);
    } catch(Exception e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
  @PutMapping("/{id}")
  // cong viec cua admin
  public ResponseEntity<?> updateOrder(
      @Valid @PathVariable Long id,
      @Valid @RequestBody OrderDTO orderDTO
  ){
    try {
      OrderResponse orderResponse = orderService.updateOrder(id, orderDTO);
      return ResponseEntity.ok(orderResponse);
    } catch (DataNotFoundException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteOrder(@Valid @PathVariable Long id) throws DataNotFoundException {
    // xoa mem ( cap nhat active = false )
    orderService.deleteOrder(id);
    return ResponseEntity.ok("order deleted successfully");
  }
}
