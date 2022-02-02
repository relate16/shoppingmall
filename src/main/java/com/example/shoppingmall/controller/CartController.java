package com.example.shoppingmall.controller;

import com.example.shoppingmall.dto.ItemDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class CartController {

    //미구현, Cart 구현하려면 로그인이 필요할 것 같은데 로그인 기능에 대해
    // 아직 배우지 않아, 구현하지 않음.
    @GetMapping("/cart")
    public String cartGet(@ModelAttribute ItemDto itemDto) {
        return "ShoppingCart";
    }
}
