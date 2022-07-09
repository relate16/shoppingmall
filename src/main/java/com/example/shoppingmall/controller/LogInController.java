package com.example.shoppingmall.controller;

import com.example.shoppingmall.constant.SessionConst;
import com.example.shoppingmall.dto.LogInDto;
import com.example.shoppingmall.entity.Member;
import com.example.shoppingmall.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LogInController {
    private final MemberRepository memberRepository;

    @GetMapping("/members/login")
    public String logInGet(@ModelAttribute LogInDto logInDto) {
        return "login/logIn";
    }

    @PostMapping("/members/login")
    public String logInPost(@Validated @ModelAttribute LogInDto logInDto, BindingResult bindingResult,
                            @RequestParam(defaultValue = "/",required = false) String redirectURL,
                            HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            log.info("error={}", bindingResult);
            return "login/logIn";
        }

        Optional<Member> findMember = memberRepository.findByUsername(logInDto.getUsername());
        if (findMember.isEmpty()) {
            bindingResult.reject("notFoundMember", "defaultMessage");
            return "login/logIn";
        }

        Member logInMember = findMember.get();
        if (logInDto.getPassword() != logInMember.getPassword()) {
            bindingResult.reject("notFoundMember", "defaultMessage");
            return "login/logIn";
        }

        //로그인 성공 이후
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, logInDto);

        return "redirect:" + redirectURL;
    }

    @PostMapping("/members/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}
