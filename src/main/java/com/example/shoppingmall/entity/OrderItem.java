package com.example.shoppingmall.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "orderPrice", "quantity"}, callSuper = true)
public class OrderItem extends BaseTimeEntity{

    @Id
    @GeneratedValue
    @Column(name = "order_Item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int quantity;

    /* discount 단위 %, 10% 할인이면 10 입력. */
    private int discount;

    /* 생성 메서드 */
    public static OrderItem createOrderItem(Item item, int quantity, int discount) {
        OrderItem orderItem = new OrderItem();

        orderItem.changeItemForCreateMethod(item);
        item.subtractQuantity(quantity);

        orderItem.changeQuantityForCreateMethod(quantity);
        orderItem.changeDiscountForCreateMethod(discount);
        orderItem.calculateOrderPrice();
        return orderItem;
    }

    /*필드 값 접근 메서드*/

    public void changeOrder(Order order) {
        this.order = order;
    }

    public void changeItemForCreateMethod(Item item) {
        this.item = item;
    }

    public void changeQuantityForCreateMethod(int quantity) {
        this.quantity = quantity;
    }

    public void changeDiscountForCreateMethod(int discount) {
        this.discount = discount;
    }

    /* 비지니스 로직 */

    /**
     * 취소
     */
    public void cancel() {
        getItem().addQuantity(getQuantity());
    }

    /**
     * 주문 가격 계산
     * 생성 메서드에 쓰이고 있음.
     */
    public int calculateOrderPrice() {
        this.orderPrice = (int) (item.getPrice() * ((100 - getDiscount()) * 0.01));
        return orderPrice;
    }


}
