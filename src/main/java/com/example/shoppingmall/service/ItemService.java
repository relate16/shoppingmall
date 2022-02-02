package com.example.shoppingmall.service;

import com.example.shoppingmall.entity.Item;
import com.example.shoppingmall.exception.EmptyResultException;
import com.example.shoppingmall.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public Item findItemById(Long itemId) {
        Optional<Item> findItemOptional = itemRepository.findById(itemId);
        Item item = findItemOptional.orElseThrow(() -> new EmptyResultException("해당하는 Item이 없습니다"));
        return item;
    }
}
