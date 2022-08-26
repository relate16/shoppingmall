package com.example.shoppingmall.service;

import com.example.shoppingmall.entity.Cart;
import com.example.shoppingmall.entity.Item;
import com.example.shoppingmall.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    /**
     * 최근에 추가한 item 순서대로 반환
     */
    public List<Item> findItemsFromCart(Cart cart) {
        if (cart.getItemIds() == "") {
            return new ArrayList<>();
        }
        List<Long> itemIdList = cart.getItemIdList();
        Collections.reverse(itemIdList);

        // JPA에서 id 중복으로 조회할 경우 db를 한 개만 가져와서 장바구니에 중복된 상품을 넣을 경우 하나 상품만 보여져서
        // iter문으로 따로 처리
        // 예) select i from item i where i.id in 1, 1, 1 -> 1개 데이터만 들고옴
        List<Item> items = new ArrayList<>();
        for (Long itemId : itemIdList) {
            Optional<Item> findItem = itemRepository.findById(itemId);
            Item item = findItem.orElseThrow(() -> new RuntimeException("해당 Item이 없습니다."));
            items.add(item);
        }
        System.out.println("items = " + items);
        return items;
    }
}
