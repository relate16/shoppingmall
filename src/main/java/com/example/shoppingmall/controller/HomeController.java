package com.example.shoppingmall.controller;

import com.example.shoppingmall.constant.SessionConst;
import com.example.shoppingmall.dto.LogInDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) LogInDto logInDto,
                       Model model) {
        if (logInDto == null) {
            return "home/home";
        }

        model.addAttribute("logInDto", logInDto);
        return "home/logInHome";
    }
}
