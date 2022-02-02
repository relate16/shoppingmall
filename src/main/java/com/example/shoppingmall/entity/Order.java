package com.example.shoppingmall.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "orderStatus"}, callSuper = true)
public class Order extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    //연관관계 편의 메서드
    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.changeOrder(this);
    }

    //생성 메서드
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItem) {
        Order order = new Order();
        //'order.필드명' 처럼 필드값 직접 접근 :
        // 프록시로 조회해해도 order.createOrder(..)처럼 쓸 건 아니기 때문에 필드명으로 직접 접근
        order.member = member;
        order.delivery=delivery;
        for (OrderItem item : orderItem) {
            order.addOrderItem(item);
        }
        order.orderStatus = OrderStatus.결제완료;
        return order;
    }
    //비지니스 로직

    /**
     * 주문취소
     */
    public void cancel() {
        if (getOrderStatus() != OrderStatus.결제완료) {
            throw new IllegalStateException("배송이 출발된 이후거나 이미 결제 취소된 상태입니다. 결제를 취소할 수 없습니다.");
        }
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
        this.orderStatus = OrderStatus.주문취소;
    }

    /**
     * 주문 총 가격 조회
     */
    public int totalPrice() {
        int totalPrice = 0;
        List<OrderItem> orderItems = getOrderItems();
        for (OrderItem orderItem : orderItems) {
            int result = orderItem.calculateOrderPrice();
            totalPrice += result;
        }
        return totalPrice;
    }

    /**
     * 배송지 변경
     */
    public void changeDelivery(Delivery delivery) {
        if (getOrderStatus() != OrderStatus.결제완료) {
            throw new IllegalStateException("배송중이거나 배송완료인 상태입니다. 배송지를 변경할 수 없습니다.");
        }
        this.delivery = delivery;
    }


}