package com.example.shoppingmall.repository;

import com.example.shoppingmall.dto.OrderDto;
import com.example.shoppingmall.dto.OrderItemDto;
import com.example.shoppingmall.entity.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.shoppingmall.entity.QDelivery.delivery;
import static com.example.shoppingmall.entity.QItem.item;
import static com.example.shoppingmall.entity.QMember.member;
import static com.example.shoppingmall.entity.QOrder.order;
import static com.example.shoppingmall.entity.QOrderItem.orderItem;

@Slf4j
@Repository
public class OrderQueryRepository {

    private final JPAQueryFactory queryFactory;

    public OrderQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * OrderDto 모든 필드변수 다 채워주는 메소드
     */
    public OrderDto findOrderDtoByOrderId(Long orderId) {

        try {
            List<OrderItem> findOrderItems = queryFactory.selectFrom(orderItem)
                    .where(orderItem.order.id.eq(orderId))
                    .join(orderItem.item, item).fetchJoin()
                    .fetch();

                    /* 페치조인은 select이 엔티티여야... 왜 깜빡하고 고생함..
                    List<OrderItemDto> findOrderItemDtos = queryFactory
                    .select(new QOrderItemDto(orderItem.id, item.title, item.filePath,
                            orderItem.id, orderItem.orderPrice, orderItem.quantity, orderItem.discount))
                    .from(orderItem)
                    .where(orderItem.order.id.eq(orderId))
                    .join(orderItem.item, item).fetchJoin()
                    .fetch();*/

        List<OrderItemDto> orderItemDtos = findOrderItems.stream()
                .map(x -> new OrderItemDto(x.getItem().getId(), x.getItem().getName(), x.getItem().getTitle(),
                        x.getItem().getFilePath(),
                        x.getId(), x.getOrderPrice(), x.getQuantity(), x.getDiscount()))
                .collect(Collectors.toList());

            Order findOrder = queryFactory.selectFrom(order)
                    .where(order.id.eq(orderId))
                    .join(order.member, member).fetchJoin().join(order.delivery, delivery).fetchJoin()
                    .orderBy(order.createdDate.desc().nullsLast())
                    .fetchFirst();

            OrderDto orderDto =
                    new OrderDto(findOrder, findOrder.getDelivery(),findOrder.getMember(), orderItemDtos);

            return orderDto;
        } catch (NullPointerException e) {
            //로그 출력 검색해서 수정하기.
            log.info("log info = {}", e);
            // Null일 경우 return 임시로 처리.
            return new OrderDto();
        }
    }

    /**
     * itemId로 OrderDto 모든 필드변수 다 채워주는 메소드, 없으면 null 반환
     */
    public OrderDto findOrderDtoByItemId(Long itemId) {
        OrderItem findOrderItem = queryFactory
                .selectFrom(QOrderItem.orderItem)
                .where(QOrderItem.orderItem.item.id.eq(itemId))
                .fetchOne();

        if (findOrderItem != null) {
            if (findOrderItem.getOrder() != null) {
                Order order = findOrderItem.getOrder();
                OrderDto orderDto = findOrderDtoByOrderId(order.getId());
                return orderDto;
            }
        }
        return null;
    }

    /**
     *  한 멤버의 전체 주문서 내용을 조회
     * List<OrderDto> 안의 OrderDto 모든 필드변수 다 채워주는 메소드
     */
    public List<OrderDto> findOrderDtosByUsername(String username) {
        List<OrderDto> orderDtos = new ArrayList<>();
        List<Order> findOrders = queryFactory.selectFrom(order)
                .join(order.member, member).fetchJoin()
                .where(member.username.eq(username))
                .fetch();

        Member member = queryFactory
                .selectFrom(QMember.member)
                .where(QMember.member.username.eq(username)).fetchOne();

        /* 이 사이트에선 order당 orderItem 하나 밖에 못 넣어서
        List<OrderItem>이 아니라 OrderItem을 가져오는 걸로 하였음. */
        for (Order order : findOrders) {
            List<OrderItem> orderItems = queryFactory.selectFrom(orderItem)
                    .where(orderItem.order.id.eq(order.getId()))
                    .join(orderItem.item, item).fetchJoin()
                    .fetch();

            List<OrderItemDto> orderItemDtos = orderItems.stream().map(x ->
                            new OrderItemDto(x.getItem().getId(), x.getItem().getName(),
                                    x.getItem().getTitle(), x.getItem().getFilePath(),
                                    x.getId(), x.getOrderPrice(), x.getQuantity(), x.getDiscount()))
                    .collect(Collectors.toList());

            OrderDto orderDto = new OrderDto(order, order.getDelivery(), member, orderItemDtos);

            orderDtos.add(orderDto);
        }

        //최근 주문 순 정렬
        List<OrderDto> sorted = orderDtos.stream()
                .sorted((a, b) -> (int) ChronoUnit.MILLIS.between(a.getOrderCreatedDate(),b.getOrderCreatedDate()))
                .collect(Collectors.toList());
        return sorted;
    }


    /**
     * 가장 최근 주문 조회
     */
    public OrderDto findLastOrderDtoByUsername(String username) {
        List<OrderDto> orderDtos = new ArrayList<>();

        List<Order> findOrders = queryFactory.selectFrom(order)
                .join(order.member, member).fetchJoin()
                .where(member.username.eq(username))
                .fetch();
        if (findOrders.size() == 0) {
            return null;
        }

        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq(username)).fetchOne();

        for (Order order : findOrders) {
            List<OrderItem> orderItems = queryFactory.selectFrom(orderItem)
                    .where(orderItem.order.id.eq(order.getId()))
                    .join(orderItem.item, item).fetchJoin()
                    .fetch();

            List<OrderItemDto> orderItemDtos = orderItems.stream().map(x ->
                            new OrderItemDto(x.getItem().getId(), x.getItem().getName(),
                                    x.getItem().getTitle(), x.getItem().getFilePath(),
                                    x.getId(), x.getOrderPrice(), x.getQuantity(), x.getDiscount()))
                    .collect(Collectors.toList());

            OrderDto orderDto = new OrderDto(order, order.getDelivery(), findMember, orderItemDtos);

            orderDtos.add(orderDto);
        }

        //최근 주문 순 정렬
        List<OrderDto> sorted = orderDtos.stream()
                .sorted((a, b) -> (int) ChronoUnit.MILLIS.between(a.getOrderCreatedDate(),b.getOrderCreatedDate()))
                .collect(Collectors.toList());

        return sorted.get(0);
    }
}
