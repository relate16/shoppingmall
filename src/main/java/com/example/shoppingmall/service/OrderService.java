package com.example.shoppingmall.service;

import com.example.shoppingmall.entity.*;
import com.example.shoppingmall.exception.NotFoundException;
import com.example.shoppingmall.repository.ItemRepository;
import com.example.shoppingmall.repository.OrderItemRepository;
import com.example.shoppingmall.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;


    @Transactional
    public Order createOrder(List<OrderItem> orderItems, Address address, Member member) {
        Order order = Order.createOrder(member, new Delivery(address), orderItems);
        orderRepository.save(order);
        return order;
    }


    @Transactional
    public void changeOrderStatus(Long orderId, OrderStatus orderStatus) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        Order order = orderOpt
                .orElseThrow(() -> new NotFoundException("orderId 에 해당하는 Order를 찾을 수 없습니다."));
        order.changeOrderStatus(orderStatus);
    }


    @Transactional
    public void cancel(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        Order order = orderOpt.orElseThrow(()-> new NotFoundException("해당 order가 없습니다."));
        order.cancel();
    }

}
