package com.example.shoppingmall.service;

import com.example.shoppingmall.entity.Cart;
import com.example.shoppingmall.entity.Member;
import com.example.shoppingmall.dto.SignUpDto;
import com.example.shoppingmall.repository.CartRepository;
import com.example.shoppingmall.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignUpService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final CartRepository cartRepository;

    @Transactional
    public Long signUp(SignUpDto signUpDto) {
        memberService.duplicateMember(signUpDto.getUsername());
        memberService.notEqualPassword(signUpDto);
        Member member = new Member(signUpDto.getUsername(), signUpDto.getPassword());
        Member savedMember = memberRepository.save(member);

        Cart cart = Cart.createdCart(member);
        cartRepository.save(cart);

        return savedMember.getId();
    }
}
