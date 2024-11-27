package com.ptit.shopapp.repositories;

import com.ptit.shopapp.models.Order;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
  // tim cac don hang cua 1 user nao do
  List<Order> findByUserId(Long userId);
  @Query("SELECT o FROM Order o WHERE (:keyword IS NULL OR :keyword = '' "
      + "OR o.fullName LIKE %:keyword% OR o.address LIKE %:keyword% OR o.note LIKE %:keyword%)")
  Page<Order> findByKeyword(String keyword, Pageable pageable);
}
