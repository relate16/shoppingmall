package com.example.shoppingmall.service;

import com.example.shoppingmall.entity.Cart;
import com.example.shoppingmall.entity.Member;
import com.example.shoppingmall.exception.NotFoundException;
import com.example.shoppingmall.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;

    @Transactional
    public void addItemId(Optional<Cart> cartOpt, Long itemId) {
        Cart cart = cartOpt.orElse(Cart.createdCart(new Member()));
        cart.addItemIdForCreateMethod(itemId);
    }

    @Transactional
    public void cleanCart(Long memberId) {
        Optional<Cart> cartOpt = cartRepository.findByMemberId(memberId);
        Cart cart = cartOpt.orElseThrow(() -> new NotFoundException("해당하는 memberId를 가진 Cart 가 없습니다."));
        cart.cleanItemList();
    }
}
