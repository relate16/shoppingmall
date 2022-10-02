package com.example.shoppingmall.service;

import com.example.shoppingmall.dto.ItemDto;
import com.example.shoppingmall.entity.Cart;
import com.example.shoppingmall.entity.Item;
import com.example.shoppingmall.exception.NotFoundException;
import com.example.shoppingmall.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    public Item findItemById(Long itemId) {
        Optional<Item> itemOpt = itemRepository.findById(itemId);
        return itemOpt.orElseThrow(()->new NotFoundException("해당 item이 없습니다."));
    }

    /**
     * 최근에 추가한 item 순서대로 반환
     */
    public List<Item> findItemsFromCart(Cart cart) {
        if (cart.getItemIds() == "") {
            return new ArrayList<>();
        }
        List<Long> itemIdList = cart.getItemIdList();
        Collections.reverse(itemIdList);

        /* JPA에서 id 중복으로 조회할 경우 db를 한 개만 가져와서 장바구니에 중복된 상품을 넣을 경우 하나 상품만 보여져서
        * iter문으로 따로 처리
        * 예) select i from item i where i.id in 1, 1, 1 -> 1개 데이터만 들고옴 */
        List<Item> items = new ArrayList<>();
        for (Long itemId : itemIdList) {
            Optional<Item> findItem = itemRepository.findById(itemId);
            Item item = findItem.orElseThrow(() -> new NotFoundException("해당 Item이 없습니다."));
            items.add(item);
        }
        return items;
    }

    public List<ItemDto> getItemDtos(List<Item> items) {
        return items.stream()
                .map(x -> new ItemDto(x.getId(), x.getName(), x.getPrice(), x.getTitle(), x.getFilePath()))
                .collect(Collectors.toList());
    }
}
