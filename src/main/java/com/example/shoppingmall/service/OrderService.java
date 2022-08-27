package com.example.shoppingmall.service;

import com.example.shoppingmall.entity.*;
import com.example.shoppingmall.exception.NotFoundException;
import com.example.shoppingmall.repository.ItemRepository;
import com.example.shoppingmall.repository.OrderItemRepository;
import com.example.shoppingmall.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final ItemService itemService;


    @Transactional
    public Order createOrder(List<OrderItem> orderItems, Address address, Member member) {
        Order order = Order.createOrder(member, new Delivery(address), orderItems);
        orderRepository.save(order);
        return order;
    }

    @Transactional
    public Order createOrder(ArrayList<Long> itemIds, ArrayList<Integer> quantities, Member member) {
        // ↓ order 생성할 때, orderItem 을 필수가 아니게 해서 DB 에서 쓸데없는 행이 추가되지 않도록 했음
        // orderItem 을 필수값으로 설정하면 Order 생성을 위해 orderItem 을 임의로 만들어야 하고
        // orderItem을 임의로 만들면 나중에 임의로 만든 orderItem 을 따로 지우던가 해야 함.
        Order order = Order.createOrder(member, new Delivery(new Address("","")),
                null);

        for (int i = 0; i < itemIds.size(); i++) {

            Long itemId = itemIds.get(i);
/*            Optional<Item> itemOpt = itemRepository.findById(itemId);
            Item item = itemOpt.orElseThrow(()->new NotFoundException("해당 item을 찾을 수 없습니다."));
            */
            Item item = itemService.findItemById(itemId);

            //↓오더 화면에 디스카운트 처리 화면이 없어서 0 처리
            OrderItem orderItem = OrderItem.createOrderItem(item, quantities.get(i), 0);
            order.addOrderItem(orderItem);
        }

        Order saveOrder = orderRepository.save(order);
        return saveOrder;
    }


    @Transactional
    public void changeOrderStatus(Long orderId, OrderStatus orderStatus) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        Order order = orderOpt
                .orElseThrow(() -> new NotFoundException("해당 order가 없습니다."));
        order.changeOrderStatus(orderStatus);
    }


    @Transactional
    public void cancel(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        Order order = orderOpt.orElseThrow(()-> new NotFoundException("해당 order가 없습니다."));
        order.cancel();
    }

}
