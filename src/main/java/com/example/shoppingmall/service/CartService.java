package com.example.shoppingmall.service;

import com.example.shoppingmall.entity.Cart;
import com.example.shoppingmall.entity.Item;
import com.example.shoppingmall.entity.Member;
import com.example.shoppingmall.exception.NotFoundException;
import com.example.shoppingmall.repository.CartRepository;
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
public class CartService {

    private final CartRepository cartRepository;

    @Transactional
    public void addItemId(Member member, Long itemId) {
        Optional<Cart> findCart = cartRepository.findByMember(member);
        Cart cart = findCart.orElseThrow(()->new NotFoundException("해당 cart가 없습니다."));
        cart.addItemIdForCreateMethod(itemId);
    }

    @Transactional
    public void cleanCart(Member member) {
        Optional<Cart> cartOpt = cartRepository.findByMember(member);
        Cart cart = cartOpt.orElseThrow(() -> new NotFoundException("해당하는 memberId를 가진 Cart 가 없습니다."));
        cart.cleanItemList();
    }

    public Cart findCartByMember(Member member) {
        Optional<Cart> cartOpt = cartRepository.findByMember(member);
        Cart cart = cartOpt.orElseThrow(()->new NotFoundException("해당 cart를 찾을 수 없습니다."));
        return cart;
    }
}
