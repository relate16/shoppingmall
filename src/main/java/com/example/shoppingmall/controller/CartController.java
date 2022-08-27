package com.example.shoppingmall.controller;

import com.example.shoppingmall.constant.SessionConst;
import com.example.shoppingmall.dto.ItemDto;
import com.example.shoppingmall.dto.LogInDto;
import com.example.shoppingmall.entity.Cart;
import com.example.shoppingmall.entity.Item;
import com.example.shoppingmall.entity.Member;
import com.example.shoppingmall.service.CartService;
import com.example.shoppingmall.service.ItemService;
import com.example.shoppingmall.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/cart")
    public String cartGet(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) LogInDto logInDto, Model model) {
        Long totalPrice = 0L;

        Member member = memberService.findMemberByUsername(logInDto.getUsername());
        Cart cart = cartService.findCartByMember(member);

        List<Item> items = itemService.findItemsFromCart(cart);
        List<ItemDto> itemDtos = itemService.getItemDtos(items);
        for (ItemDto itemDto : itemDtos) {
            totalPrice += itemDto.getPrice();
        }

        model.addAttribute("itemDtos", itemDtos);
        model.addAttribute("totalPrice", totalPrice);
        return "cart/shoppingCart";
    }

    @PostMapping("/cart/add")
    public String addProductToCart(@RequestParam Long itemId, @RequestParam int page,
                                   @SessionAttribute(name = SessionConst.LOGIN_MEMBER) LogInDto logInDto) {
        Member member = memberService.findMemberByUsername(logInDto.getUsername());
        cartService.addItemId(member, itemId);
        return "redirect:/products/detail?id=" + itemId +"&page=" + page ;
    }

}
