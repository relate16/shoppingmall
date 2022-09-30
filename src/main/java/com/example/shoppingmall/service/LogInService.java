package com.example.shoppingmall.service;

import com.example.shoppingmall.entity.Member;
import com.example.shoppingmall.dto.LogInDto;
import com.example.shoppingmall.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LogInService {
    private final MemberRepository memberRepository;

    public Member logIn(LogInDto logInDto) {
        Optional<Member> findMember = memberRepository.findByUsername(logInDto.getUsername());
        Member member = findMember.orElseGet(() -> new Member(null, 0));
        /* ↑ findMember가 Optional.empty일 때 값을 꺼낼 경우
        어떻게 처리해야 할 지 잘 몰라, 임기응변으로 필드값 넣지 않은 Member 생성 */

        if (member.getPassword() == logInDto.getPassword()) {
            return member;
        } else {
            throw new IllegalStateException("해당하는 이름과 패스워드가 일치하지 않습니다.");
        }

    }

}
