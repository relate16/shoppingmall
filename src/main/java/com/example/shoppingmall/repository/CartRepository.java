package com.example.shoppingmall.repository;

import com.example.shoppingmall.entity.Cart;
import com.example.shoppingmall.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByMember(Member member);
}
