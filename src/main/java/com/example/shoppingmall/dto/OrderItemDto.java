package com.example.shoppingmall.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItemDto {

    private Long itemId;
    private String itemName;
    private String title;
    private String filePath;

    private Long orderItemId;
    private int orderPrice;
    private int quantity;
    private int discount;
    private int totalPrice;

    @QueryProjection
    public OrderItemDto(Long itemId, String itemName, String title, String filePath, Long orderItemId, int orderPrice,
                        int quantity, int discount) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.title = title;
        this.filePath = filePath;
        this.orderItemId = orderItemId;
        this.orderPrice = orderPrice;
        this.quantity = quantity;
        this.discount = discount;
        if (discount == 0) {
            this.totalPrice = orderPrice * quantity;
        } else {
            this.totalPrice = (int) orderPrice * quantity * (1 - discount / 100);
        }

    }
}
