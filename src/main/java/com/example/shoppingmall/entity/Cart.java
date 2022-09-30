package com.example.shoppingmall.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {
    @Id @GeneratedValue
    @Column(name = "cart_id")
    private Long id;

    private String itemIds = "";

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    /* 생성 메서드 */
    public static Cart createdCart(Member member, Long... itemId) {
        Cart cart = new Cart();
        cart.changeMemberForCreateMethod(member);
        if (itemId != null) {
            for (Long id : itemId) {
                cart.addItemIdForCreateMethod(id);
            }
        }
        return cart;
    }

    /* 필드 값 접근 메서드*/

    public void changeMemberForCreateMethod(Member member) {
        this.member = member;
    }
    public void addItemIdForCreateMethod(Long itemId) {
        /* DB에 itemIds가 ",9,3" 처럼 되는 걸 방지하기 위해 if문 추가.
        if문 추가 전 코드 : this.itemIds += "," + itemId */
        if (this.itemIds == "") {
            this.itemIds += itemId;
        } else {
            this.itemIds += "," + itemId;
        }
    }

    /* 비지니스 로직 */

    public List<Long> getItemIdList() {
        List<Long> orderItemIds = new ArrayList<>();
        String orderItemId = getItemIds();
        String[] split = orderItemId.split(",");
        for (String s : split) orderItemIds.add(Long.valueOf(s));
        return orderItemIds;
    }

    public void cleanItemList() {
        this.itemIds = "";
    }


}
