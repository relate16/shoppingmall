package com.example.shoppingmall.entity;

import com.example.shoppingmall.exception.NotEnoughQuantityException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name", "price", "quantity", "title", "filePath"})
public class Item extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;
    private int quantity;

    private String title;
    private String filePath;

    public Item(String name, int price, int quantity, String title, String filePath) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.title = title;
        this.filePath = filePath;
    }

    /* 비지니스 로직 */

    /**
     * 수량 추가
     */
    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    /**
     * 수량 제거
     */
    public void subtractQuantity(int quantity) {
        int subtractedQuantity = this.quantity - quantity;
        if (subtractedQuantity < 0) {
            throw new NotEnoughQuantityException("재고가 없습니다.");
        } else {
            this.quantity = subtractedQuantity;
        }
    }

}
