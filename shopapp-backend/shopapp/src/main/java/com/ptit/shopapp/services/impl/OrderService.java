package com.ptit.shopapp.services.impl;

import com.ptit.shopapp.dtos.OrderDTO;
import com.ptit.shopapp.exceptions.DataNotFoundException;
import com.ptit.shopapp.models.Order;
import com.ptit.shopapp.models.OrderStatus;
import com.ptit.shopapp.models.User;
import com.ptit.shopapp.repositories.OrderRepository;
import com.ptit.shopapp.repositories.UserRepository;
import com.ptit.shopapp.responses.OrderResponse;
import com.ptit.shopapp.services.IOrderService;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
  private final OrderRepository orderRepository;
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;
  @Override
  public OrderResponse createOrder(OrderDTO orderDTO) throws Exception {
    Order order = new Order();
    // tim xem userId co ton tai ko
    User user = userRepository
        .findById(orderDTO.getUserId())
        .orElseThrow(() -> new DataNotFoundException(
            "cannot find user with id = " + orderDTO.getUserId()));
    // convert userDTO sang order
    // dung thu vien Model mapper
    // tao mot luong bang anh xa rieng de kiem soat viec anh xa
    modelMapper.typeMap(OrderDTO.class, Order.class)
        .addMappings(mapper -> mapper.skip(Order::setId));
    // cap nhat cac truong cua don hang tu orderDTO
    modelMapper.map(orderDTO, order);
    order.setUser(user);
    order.setOrderDate(new Date());
    order.setStatus(OrderStatus.PENDING);
    // kiem tra shipping date phai >= ngay hom nay
    LocalDate shippingDate = orderDTO.getShippingDate() ==
        null ? LocalDate.now() : orderDTO.getShippingDate();
    if(shippingDate == null || shippingDate.isBefore(LocalDate.now())){
      throw new DataNotFoundException("date must be at least today");
    }
    order.setShippingDate(shippingDate);
    order.setActive(true);
    orderRepository.save(order);
    modelMapper.typeMap(Order.class, OrderResponse.class);
    OrderResponse orderResponse = new OrderResponse();
    modelMapper.map(order, orderResponse);
    return orderResponse;
  }

  @Override
  public OrderResponse getOrderById(long id) {
    Optional<Order> order = orderRepository.findById(id);
    modelMapper.typeMap(Order.class, OrderResponse.class);
    OrderResponse orderResponse = new OrderResponse();
    if (order.isPresent()) {
      modelMapper.map(order.get(), orderResponse);
    }
    return orderResponse;
  }

  @Override
  public List<OrderResponse> findByUserId(Long userId) {
    List<Order> orderList = orderRepository.findByUserId(userId);

    List<OrderResponse> orderResponseList = new ArrayList<>();
    for(Order order : orderList){
      OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);
      orderResponseList.add(orderResponse);
    }
    return orderResponseList;
  }

  @Override
  @Transactional
  public OrderResponse updateOrder(long id, OrderDTO orderDTO) throws DataNotFoundException {
    Order existingOrder = orderRepository.findById(id)
        .orElseThrow(() -> new DataNotFoundException("cannot find order with id = " + id));
    if(existingOrder != null){
      User user = userRepository
          .findById(orderDTO.getUserId())
          .orElseThrow(() -> new DataNotFoundException(
              "cannot find user with id = " + orderDTO.getUserId()));
      modelMapper.typeMap(OrderDTO.class, Order.class)
          .addMappings(mapper -> mapper.skip(Order::setId));
      modelMapper.map(orderDTO, existingOrder);
      existingOrder.setUser(user);
      orderRepository.save(existingOrder);
      OrderResponse orderResponse = new OrderResponse();
      modelMapper.map(existingOrder, orderResponse);
      return orderResponse;
    }
    return null;
  }

  @Override
  public void deleteOrder(long id) throws DataNotFoundException {
    Order existingOrder = orderRepository.findById(id).orElse(null);
    if(existingOrder != null){
      existingOrder.setActive(false);
      orderRepository.save(existingOrder);
    }
  }
}
