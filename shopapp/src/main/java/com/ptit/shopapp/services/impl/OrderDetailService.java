package com.ptit.shopapp.services.impl;

import com.ptit.shopapp.dtos.OrderDetailDTO;
import com.ptit.shopapp.exceptions.DataNotFoundException;
import com.ptit.shopapp.models.Order;
import com.ptit.shopapp.models.OrderDetail;
import com.ptit.shopapp.models.Product;
import com.ptit.shopapp.repositories.OrderDetailRepository;
import com.ptit.shopapp.repositories.OrderRepository;
import com.ptit.shopapp.repositories.ProductRepository;
import com.ptit.shopapp.services.IOrderDetailService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {
  private final OrderDetailRepository orderDetailRepository;
  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;
  @Override
  @Transactional
  public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
    Order order = orderRepository.findById(orderDetailDTO.getOrderId())
        .orElseThrow(() -> new DataNotFoundException(
            "cannot find order with id = " + orderDetailDTO.getOrderId()));
    Product product = productRepository.findById(orderDetailDTO.getProductId())
        .orElseThrow(() -> new DataNotFoundException(
            "cannot find product with id = " + orderDetailDTO.getProductId()));
    OrderDetail orderDetail = OrderDetail
        .builder()
        .order(order)
        .product(product)
        .price(orderDetailDTO.getPrice())
        .numberOfProduct(orderDetailDTO.getNumberOfProduct())
        .totalMoney(orderDetailDTO.getTotalMoney())
        .color(orderDetailDTO.getColor())
        .build();
    return orderDetailRepository.save(orderDetail);
  }

  @Override
  public OrderDetail getOrderDetail(Long id) throws DataNotFoundException {
    return orderDetailRepository.findById(id)
        .orElseThrow(() -> new DataNotFoundException(
            "cannot find orderdetail with id = " + id));
  }

  @Override
  @Transactional
  public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO)
      throws DataNotFoundException {
    // tim order detail co ton tai ko
    OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
        .orElseThrow(() -> new DataNotFoundException(
            "cannot find orderDetail with id = " + id));
    Order order = orderRepository.findById(orderDetailDTO.getOrderId())
        .orElseThrow(() -> new DataNotFoundException(
            "cannot find order with id = " + orderDetailDTO.getOrderId()));
    Product product = productRepository.findById(orderDetailDTO.getProductId())
        .orElseThrow(() -> new DataNotFoundException(
            "cannot find product with id = " + orderDetailDTO.getProductId()));
    existingOrderDetail.setOrder(order);
    existingOrderDetail.setProduct(product);
    existingOrderDetail.setPrice(orderDetailDTO.getPrice());
    existingOrderDetail.setNumberOfProduct(orderDetailDTO.getNumberOfProduct());
    existingOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
    existingOrderDetail.setColor(orderDetailDTO.getColor());
    return orderDetailRepository.save(existingOrderDetail);
  }

  @Override
  @Transactional
  public void deleteOrderDetail(Long id) {
    orderDetailRepository.deleteById(id);
  }

  @Override
  public List<OrderDetail> findByOrderId(Long orderId) {
    return orderDetailRepository.findByOrderId(orderId);
  }
}
