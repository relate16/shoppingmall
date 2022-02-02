package com.example.shoppingmall.controller;

import com.example.shoppingmall.entity.*;
import com.example.shoppingmall.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitBase {

    private final InitService initService;
    private final InitPostService initPostService;

    @PostConstruct
    public void init() {
        initService.init();
        initPostService.init();
    }

    @Component
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;
        private final MemberService memberService;

        @Transactional
        public void init() {
            for (int i = 0; i < 10; i++) {
                Address address = new Address("중앙로" + i + "길", String.valueOf(i));
                Member member = new Member("member" + i, i, address);
                em.persist(member);

                Item item = new Item("cat's tower" + i, (i + 1) * 50, (i + 1) * 10,
                        "Cat's Tower v." + i, "img/catTower" + i + ".jpg");
                em.persist(item);

                //member0에 Order 집합.
                Member findMember = memberService.findMemberByUsername("member0");
                OrderItem orderItem = OrderItem.createOrderItem(item, i + 1, 0);
                Delivery delivery = new Delivery(address);
                Order order = Order.createOrder(findMember, delivery, orderItem);
                em.persist(order);

            }
        }
    }

    @Transactional
    @Component
    @RequiredArgsConstructor
    static class InitPostService {
        private final EntityManager em;


        //@SnakeyThrows 기능 잘 모름.
        //게시물 최근 순서 정렬 때문에, 일단 Thread.sleep(100);을 쓰기 위해 사용함.
        @SneakyThrows
        public void init() {
            for (int i = 0; i < 13; i++) {
                Thread.sleep(100);
                Post post = new Post("게시글" + i, i + "번째 게시글 내용입니다", "writer" + i + "님", 0);
                em.persist(post);
            }
        }
    }

}
