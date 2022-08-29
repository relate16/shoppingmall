package com.example.shoppingmall.controller;

import com.example.shoppingmall.entity.*;
import com.example.shoppingmall.repository.CartRepository;
import com.example.shoppingmall.repository.ItemRepository;
import com.example.shoppingmall.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Profile("test")
@Component
@RequiredArgsConstructor
@Slf4j
public class InitBaseTest {

    private final InitService initService;
    private final InitPostService initPostService;

    @PostConstruct
    public void init() throws InterruptedException {
        log.info("initBaseTest가 실행되었습니다.");
        initService.init();
        initPostService.init();
    }

    @Component
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;
        private final CartRepository cartRepository;

        @Transactional
        public void init() {
            for (int i = 0; i < 10; i++) {
                Address address = new Address("중앙로" + i + "길", String.valueOf(i));
                Member member = new Member("member" + i, i, address);
                em.persist(member);

                Cart cart = Cart.createdCart(member);
                cartRepository.save(cart);

                Item item = new Item("cat's tower" + i, (i + 1) * 50, (i + 1) * 10,
                        "Cat's Tower v." + i, "img/catTower" + i + ".jpg");
                em.persist(item);
            }
        }
    }

    @Transactional
    @Component
    @RequiredArgsConstructor
    static class InitPostService {
        private final EntityManager em;

        public void init() throws InterruptedException {
            for (int i = 0; i < 13; i++) {
                Thread.sleep(100);
                Post post = new Post("게시글" + i, i + "번째 게시글 내용입니다", "writer" + i + "님", 0);
                em.persist(post);
            }
        }
    }

}
