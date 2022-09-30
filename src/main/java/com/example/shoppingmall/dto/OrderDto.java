package com.example.shoppingmall.dto;

import com.example.shoppingmall.entity.Delivery;
import com.example.shoppingmall.entity.Member;
import com.example.shoppingmall.entity.Order;
import com.example.shoppingmall.entity.OrderStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * calculateTotalPrice() 메서드 주의
 * model 에 OrderDto 전달하기 전, 필수로 실행해줘야 함.
 */
@Data
@NoArgsConstructor
public class OrderDto {

    private Long orderId;
    @NotEmpty
    private String username;
    @NotEmpty
    private String city;
    @NotEmpty
    private String zipcode;

    private OrderStatus orderStatus;
    private LocalDateTime orderCreatedDate;

    private int totalPrice = 0;

    private List<OrderItemDto> orderItemDtos = new ArrayList<>();

    @QueryProjection
    public OrderDto(Order order, Delivery delivery, Member member, List<OrderItemDto> orderItemDtos) {
        this.orderId = order.getId();
        this.orderStatus = order.getOrderStatus();
        this.orderCreatedDate = order.getCreatedDate();
        this.username = member.getUsername();
        if (delivery != null) {
            this.city = delivery.getAddress().getCity();
            this.zipcode = delivery.getAddress().getZipcode();
        }
        this.orderItemDtos = orderItemDtos;
    }

    // ↓ model에 넣어 전달하기 전 필수로 해줘야 함.
    public void calculateTotalPrice() {
        for (OrderItemDto orderItemDto : orderItemDtos) {
            int subPrice;
            if (orderItemDto.getDiscount() == 0) {
                subPrice =
                        orderItemDto.getOrderPrice() * orderItemDto.getQuantity();
            } else {
                subPrice =
                        (int) orderItemDto.getOrderPrice() * orderItemDto.getQuantity()
                                * (1 - orderItemDto.getDiscount() / 100);
            }
            totalPrice += subPrice;
        }
    }
}
