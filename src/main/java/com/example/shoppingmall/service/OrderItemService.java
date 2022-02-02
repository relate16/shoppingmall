package com.example.shoppingmall.service;

import com.example.shoppingmall.entity.OrderItem;
import com.example.shoppingmall.exception.EmptyResultException;
import com.example.shoppingmall.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public OrderItem findOrderItemByOrderId(Long orderId) {
        Optional<OrderItem> findOrderItem = orderItemRepository.findOrderItemByOrderId(orderId);
        OrderItem orderItem = findOrderItem.orElseThrow(() -> new EmptyResultException("해당itemId에 해당하는 OrderItem이 없습니다."));
        return orderItem;
    }
}
