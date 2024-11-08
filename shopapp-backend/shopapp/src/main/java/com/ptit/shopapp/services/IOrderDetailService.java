package com.ptit.shopapp.services;

import com.ptit.shopapp.dtos.OrderDetailDTO;
import com.ptit.shopapp.exceptions.DataNotFoundException;
import com.ptit.shopapp.models.OrderDetail;
import java.util.List;

public interface IOrderDetailService {
  OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFoundException;
  OrderDetail getOrderDetail(Long id) throws DataNotFoundException;
  OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO)
      throws DataNotFoundException;
  void deleteOrderDetail(Long id);
  List<OrderDetail> findByOrderId(Long orderId);

}
