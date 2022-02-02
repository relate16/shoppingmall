package com.example.shoppingmall.service;

import com.example.shoppingmall.entity.*;
import com.example.shoppingmall.exception.EmptyResultException;
import com.example.shoppingmall.repository.ItemRepository;
import com.example.shoppingmall.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final ItemService itemService;


    public Order findOrderById(Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        Order order = orderOptional.orElseThrow(() -> new EmptyResultException("해당 id에 해당하는 Order가 없습니다."));
        return order;
    }

    @Transactional
    public Order createOrderByItemIdAddressMember(Long itemId, Address address, Member member) {
        Delivery delivery = new Delivery(address);
        Item item = itemService.findItemById(itemId);
        //↓ products/detail 화면에 수량 및 discount 변경하는 게 없어서 quantity=1&discount=0처리.
        OrderItem orderItem = OrderItem.createOrderItem(item, 1, 0);
        Order order = Order.createOrder(member, delivery, orderItem);
        orderRepository.save(order);
        return order;
    }

    @Transactional
    public void cancel(Long orderId) {
        Order findOrder = findOrderById(orderId);
        findOrder.cancel();
    }

}
