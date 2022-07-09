package com.example.shoppingmall.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
public class ItemDto {
    private Long itemId;
    @NotEmpty
    private String name;
    @PositiveOrZero
    private int price;
    @NotEmpty
    private String title;
    private String filePath;

    @QueryProjection
    public ItemDto(Long itemId, String name, int price, String title, String filePath) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.title = title;
        this.filePath = filePath;
    }
}
