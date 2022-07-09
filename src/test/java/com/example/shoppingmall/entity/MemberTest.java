package com.example.shoppingmall.entity;

import com.example.shoppingmall.repository.ItemRepository;
import com.example.shoppingmall.repository.MemberRepository;
import com.example.shoppingmall.repository.OrderQueryRepository;
import com.example.shoppingmall.repository.OrderRepository;
import com.example.shoppingmall.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MemberTest {
    @Autowired
    EntityManager em;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderService orderService;

    @Autowired
    OrderQueryRepository orderQueryRepository;

    @Test
    public void test(){
        Member memberA = new Member("memberG", 1234);
        em.persist(memberA);
        em.flush();
        em.clear();
        Optional<Member> findMember = memberRepository.findByUsername("memberF");
        Member member = findMember.orElseGet(()->new Member());
        System.out.println("member = " + member.getPassword());

    }


    @Test
    public void test3() {
        Address address = new Address("seoul", "123");
        Member member = new Member("memberA", 123,address);
        em.persist(member);
        Delivery delivery = new Delivery(address);
        Item item = new Item("itemA", 1000, 10, "hi", "hi");
        em.persist(item);
        OrderItem orderItem = OrderItem.createOrderItem(item, 2, 0);
        Item item2 = new Item("itemB", 1000, 10, "hi", "hi");
        em.persist(item);
        OrderItem orderItem2 = OrderItem.createOrderItem(item, 2, 0);
/*        Order order = Order.createOrder(member, delivery, orderItem, orderItem2);
        em.persist(order);
        em.flush();
        em.clear();
        System.out.println(" =================== ");*/

    }


}