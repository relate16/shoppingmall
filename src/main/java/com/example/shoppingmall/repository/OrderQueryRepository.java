package com.example.shoppingmall.repository;

import com.example.shoppingmall.dto.OrderDto;
import com.example.shoppingmall.entity.Item;
import com.example.shoppingmall.entity.Order;
import com.example.shoppingmall.entity.OrderItem;
import com.example.shoppingmall.entity.QOrderItem;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
            OrderItem findOrderItem = queryFactory.selectFrom(orderItem)
                    .where(orderItem.order.id.eq(orderId))
                    .join(orderItem.item, item).fetchJoin()
                    .fetchOne();
            Order findOrder = queryFactory.selectFrom(order)
                    .where(order.id.eq(orderId))
                    .join(order.member, member).fetchJoin().join(order.delivery, delivery).fetchJoin()
                    .fetchOne();

            OrderDto orderDto = new OrderDto(findOrder, findOrder.getDelivery(),findOrder.getMember(),
                    findOrderItem.getItem(), findOrderItem);

            return orderDto;
        } catch (NonUniqueResultException | NullPointerException e) {
            //로그 출력 검색해서 수정하기.
            System.out.println("e = " + e);
            //결과 값이 둘 이상이나, Null일 경우 return 임시로 처리.
            // 보통 .fetchOne() null처리나 NonUniqueException 처리 어떻게 하나 알아보기.
            return new OrderDto();
        }
    }

    /**
     * List<OrderDto> 안의 OrderDto 모든 필드변수 다 채워주는 메소드
     */
    public List<OrderDto> findOrderDtosByUsername(String username) {
        List<OrderDto> orderDtos = new ArrayList<>();
        List<Order> findOrders = queryFactory.selectFrom(order)
                .join(order.member, member).fetchJoin()
                .where(member.username.eq(username))
                .fetch();

        /* 이 사이트에선 order당 orderItem 하나 밖에 못 넣어서
        List<OrderItem>이 아니라 OrderItem을 가져오는 걸로 하였음. */
        for (Order order : findOrders) {
            OrderItem orderItem = queryFactory.selectFrom(QOrderItem.orderItem)
                    .where(QOrderItem.orderItem.order.id.eq(order.getId()))
                    .join(QOrderItem.orderItem.item, item).fetchJoin()
                    .fetchOne();

            // orderItem이 null인 경우, 처리.
            // .fetchOne()했는데 null처리 어떻게 해야 하나 고민하고 넘어가려다가
            // intellJ에서 추천해줘서 아.. 이렇게 하면 되겠구나 하고 상황에 맞춰서 나머지 코드 넣어봄.
            // 보통 .fetchOne() null처리나 NonUniqueException 처리 어떻게 하나 알아보기.
            OrderDto orderDto = new OrderDto(order, order.getDelivery(), order.getMember(),
                    orderItem != null ? orderItem.getItem() : new Item(),
                    orderItem != null ? orderItem : OrderItem.createOrderItem(new Item(), 0, 0));
            orderDtos.add(orderDto);
        }

        //최근 주문 순 정렬
        List<OrderDto> sorted = orderDtos.stream()
                .sorted((a, b) -> (int) ChronoUnit.MILLIS.between(a.getOrderCreatedDate(),b.getOrderCreatedDate()))
                .collect(Collectors.toList());
        return sorted;
    }
}
