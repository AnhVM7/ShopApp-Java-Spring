package com.ptit.shopapp.repositories;

import com.ptit.shopapp.models.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
  // tim cac don hang cua 1 user nao do
  List<Order> findByUserId(Long userId);
}
