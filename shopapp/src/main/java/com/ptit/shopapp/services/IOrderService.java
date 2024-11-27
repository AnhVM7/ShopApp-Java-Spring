package com.ptit.shopapp.services;

import com.ptit.shopapp.dtos.OrderDTO;
import com.ptit.shopapp.exceptions.DataNotFoundException;
import com.ptit.shopapp.models.Order;
import com.ptit.shopapp.responses.OrderResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IOrderService {
  OrderResponse createOrder(OrderDTO orderDTO) throws Exception;
  OrderResponse getOrderById(long id);
  List<OrderResponse> findByUserId(Long userId);
  OrderResponse updateOrder(long id, OrderDTO orderDTO) throws DataNotFoundException;
  void deleteOrder(long id) throws DataNotFoundException;

  Page<Order> getOrderByKeyword(String keyword, Pageable pageable);
}
