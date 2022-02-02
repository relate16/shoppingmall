package com.example.shoppingmall.dto;

import com.example.shoppingmall.entity.*;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class OrderDto {

    private Long orderId;
    private OrderStatus orderStatus;
    private LocalDateTime orderCreatedDate;
    private String username;
    private Long itemId;
    private String itemName;
    private String imgFilePath;
    private int quantity;
    private int orderPrice;
    private int totalPrice;


    private String city;
    private String zipcode;

    /* ↓
    OrderDto(Order order, OrderItem orderItem) {
        this.orderId = ..
        this.orderStatus = ..
        this.username = order.getMember.getUsername();
        this.address = order.getMember.getAddress();
        this.itemId = OrderItem.getItem.getId();
        this.itemName = OrderItem.getItem.getName();
        this.quantity = ..
        this.orderPrice = ..
        this.totalPrice = ..
    }
        처럼 Order와 OrderItem 두 개의 매개변수로 처리하려다가
        fetchJoin 없이 Order, OrderItem 넣을 수도 있을 것 같아 아래처럼 코드 재변경.
        order.id, .. 필드변수보다 객체로 넣는게
        나중에 필드를 추가해도, 생성자에서는 매개변수에는 손 안 댈 수 있고, 생성자를 쓰는 곳은 아예 손 안댈 수 있어서 좋은 것 같음.
    */
    @QueryProjection
    public OrderDto(Order order, Delivery delivery, Member member, Item item, OrderItem orderItem) {

        this.orderId = order.getId();
        this.orderStatus = order.getOrderStatus();
        this.orderCreatedDate = order.getCreatedDate();
        this.username = member.getUsername();
        this.city = delivery.getAddress().getCity();
        this.zipcode = delivery.getAddress().getZipcode();
        this.itemId = item.getId();
        this.itemName = item.getName();
        this.imgFilePath = item.getFilePath();
        this.quantity = orderItem.getQuantity();
        this.orderPrice = orderItem.getOrderPrice();
        this.totalPrice = orderPrice * quantity;
    }
}
