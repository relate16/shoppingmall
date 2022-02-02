package com.example.shoppingmall.service;

import com.example.shoppingmall.entity.Member;
import com.example.shoppingmall.dto.SignUpDto;
import com.example.shoppingmall.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignUpService {

    private final MemberRepository memberRepository;
    public final MemberService memberService;

    @Transactional
    public Long signUp(SignUpDto signUpDto) {
        memberService.duplicateMember(signUpDto.getUsername());
        memberService.equalPassword(signUpDto);
        Member member = new Member(signUpDto.getUsername(), signUpDto.getPassword());
        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }
}
