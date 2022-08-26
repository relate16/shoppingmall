package com.example.shoppingmall.controller;

import com.example.shoppingmall.constant.SessionConst;
import com.example.shoppingmall.dto.ItemDto;
import com.example.shoppingmall.dto.LogInDto;
import com.example.shoppingmall.entity.Cart;
import com.example.shoppingmall.entity.Item;
import com.example.shoppingmall.entity.Member;
import com.example.shoppingmall.exception.NotFoundException;
import com.example.shoppingmall.repository.CartRepository;
import com.example.shoppingmall.repository.ItemRepository;
import com.example.shoppingmall.repository.MemberRepository;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;

    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/cart")
    public String cartGet(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) LogInDto logInDto, Model model) {
        Long totalPrice = 0L;

        Member member = memberService.findMemberByUsername(logInDto.getUsername());
        Cart cart = cartService.findCartByMember(member);

        List<Item> items = itemService.findItemsFromCart(cart);
        List<ItemDto> itemDtos = items.stream()
                .map(x -> new ItemDto(x.getId(), x.getName(), x.getPrice(), x.getTitle(), x.getFilePath()))
                .collect(Collectors.toList());
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
        Optional<Member> memberOpt = memberRepository.findByUsername(logInDto.getUsername());
        Member member = memberOpt.orElseThrow(() -> new NotFoundException("해당 멤버를 찾을 수 없습니다."));

        cartService.addItemId(member, itemId);
        return "redirect:/products/detail?id=" + itemId +"&page=" + page ;
    }

}
