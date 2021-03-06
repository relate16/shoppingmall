package com.example.shoppingmall.repository;

import com.example.shoppingmall.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("select oi from OrderItem oi join fetch oi.order o where o.id = :orderId")
    List<OrderItem> findOrderItemsByOrderId(@Param("orderId") Long orderId);
}
