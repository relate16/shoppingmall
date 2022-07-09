package com.example.shoppingmall.controller;

import com.example.shoppingmall.dto.SignUpDto;
import com.example.shoppingmall.service.SignUpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;

    @GetMapping("/members/new")
    public String signUpGet(@ModelAttribute SignUpDto signUpDto) {
       return "signup/signUp";
    }

    @PostMapping("/members/new")
    public String signUpPost(@Valid @ModelAttribute SignUpDto signUpDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup/signUp";
        }

        // ↓ 이미 존재하는 회원이거나 비밀번호 최초 입력 != 비밀번호 재입력 일 경우 globalError
        try {
            signUpService.signUp(signUpDto);
        } catch (IllegalStateException e) {
            if (e.getMessage().equals("이미 쓰고 있는 회원 이름입니다.")) {
                bindingResult.reject("duplicateMember", "이미 존재합니다.");
            } else {
                bindingResult.reject("notEqualPassword", "처음과 재입력한 비밀번호가 다릅니다.");
            }
            if (bindingResult.hasErrors()) {
                return "signup/signUp";
            }
        }
        return "redirect:/members/login";
    }
}
