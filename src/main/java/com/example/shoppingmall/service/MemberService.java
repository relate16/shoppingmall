package com.example.shoppingmall.service;

import com.example.shoppingmall.entity.Member;
import com.example.shoppingmall.dto.SignUpDto;
import com.example.shoppingmall.exception.NotFoundException;
import com.example.shoppingmall.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findMemberByUsername(String username) {
        Optional<Member> findMember = memberRepository.findByUsername(username);
        return findMember.orElseThrow(
                () -> new NotFoundException("해당 member가 없습니다"));
    }

    public void duplicateMember(String username) {
        try {
            findMemberByUsername(username);
            throw new IllegalStateException("이미 쓰고 있는 회원 이름입니다.");
        } catch (NotFoundException e) {
        }
    }

    public void notEqualPassword(SignUpDto signUpDto) {
        if (signUpDto.getPassword() != signUpDto.getPassword_re()) {
            throw new IllegalStateException("처음 입력한 비밀번호와 재입력한 비밀번호가 다릅니다.");
        }
    }
}
