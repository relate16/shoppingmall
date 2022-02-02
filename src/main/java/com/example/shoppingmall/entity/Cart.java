package com.example.shoppingmall.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {
    @Id @GeneratedValue
    private Long id;

    private String orderItemIds = "";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    //생성 메서드
    public static Cart createdCart(Member member, Long... orderItemId) {
        Cart cart = new Cart();
        cart.changeMemberForCreateMethod(member);
        for (Long id : orderItemId) {
            cart.addOrderItemIdForCreateMethod(id);
        }
        return cart;
    }

    //필드 값 접근 메서드
    public void changeMemberForCreateMethod(Member member) {
        this.member = member;
    }
    public void addOrderItemIdForCreateMethod(Long id) {
        this.orderItemIds += ","+id;
    }

    //비지니스 메서드
    public List<Long> getOrderItemIdList() {
        List<Long> orderItemIds = new ArrayList<>();
        String orderItemId = getOrderItemIds();
        String[] split = orderItemId.split(",");
        for (String s : split) orderItemIds.add(Long.valueOf(s));
        return orderItemIds;
    }


}
