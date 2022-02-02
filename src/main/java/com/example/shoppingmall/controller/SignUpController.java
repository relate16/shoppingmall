package com.example.shoppingmall.controller;

import com.example.shoppingmall.dto.SignUpDto;
import com.example.shoppingmall.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;

    @GetMapping("/members/new")
    public String signUpGet(@ModelAttribute SignUpDto signUpDto) {
        return "signUp";
    }

    @PostMapping("/members/new")
    public String signUpPost(@Valid @ModelAttribute SignUpDto signUpDto) {
        try {
            signUpService.signUp(signUpDto);
        } catch (IllegalStateException e) {
            //로그 처리 방법 검색해서 채워넣기.
            System.out.println("e.getMessage() = " + e.getMessage());
            return "redirect:/members/new";
        }
        return "redirect:/members/login";
    }
}
