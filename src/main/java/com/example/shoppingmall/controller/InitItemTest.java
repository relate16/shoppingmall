package com.example.shoppingmall.controller;

import com.example.shoppingmall.entity.Item;
import com.example.shoppingmall.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Profile("test")
@Component
@RequiredArgsConstructor
public class InitItemTest {

    private final InitItemService initItemService;

    @PostConstruct
    public void init() {
        initItemService.init();
    }

    @Component
    @RequiredArgsConstructor
    static class InitItemService {

        private final ItemRepository itemRepository;

        @Transactional
        public void init() {
            for (int i = 0; i < 10; i++) {
                itemRepository.save(
                        new Item("cat's tower" + i, (i + 1) * 50, i + 1,
                                "Cat's Tower v." + i, "img/catTower" + i + ".jpg"));
            }
        }
    }

}
