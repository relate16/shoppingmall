package com.example.shoppingmall.dto;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class CartDto {

    private Long cartId;
    private String itemIds = "";
    private Long memberId;

    @QueryProjection
    public CartDto(Long cartId, String itemIds, Long memberId) {
        this.cartId = cartId;
        this.itemIds = itemIds;
        this.memberId = memberId;
    }
}
