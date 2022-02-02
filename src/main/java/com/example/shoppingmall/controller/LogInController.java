package com.example.shoppingmall.controller;

import com.example.shoppingmall.dto.LogInDto;
import com.example.shoppingmall.service.LogInService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class LogInController {
    private final LogInService logInService;

    @GetMapping("/members/login")
    public String logInGet(@ModelAttribute LogInDto logInDto) {
        return "logIn";
    }

    @PostMapping("/members/login")
    public String logInPost(@Valid @ModelAttribute LogInDto logInDto) {
        //로그인 기능 아직 안배운 상태라서 임시구현.
        //bindingResult처리 안함 강의 들으면 처리하기

        try {
            logInService.logIn(logInDto);
            return "redirect:/home";
        } catch (IllegalStateException e) {
            //로그 처리 방법 검색해서 채워넣기.
            System.out.println("e.getMessage() = " + e.getMessage());
            return "redirect:/members/login";
        }
    }

}
