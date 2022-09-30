package com.example.shoppingmall.controller;

import com.example.shoppingmall.dto.ItemDto;
import com.example.shoppingmall.repository.ItemQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class ProductsController {

    private final ItemQueryRepository itemQueryRepository;

    @GetMapping("/products")
    public String productsGet(@ModelAttribute ItemDto itemDto, Model model, Pageable pageable) {
        Page<ItemDto> page = itemQueryRepository.showPageList(pageable);
        model.addAttribute("page", page);
        return "item/products";
    }

    @GetMapping("/products/detail")
    public String productGet(@RequestParam(value = "id") Long itemId,
                             @RequestParam(value = "page") int pageNumber,
                             Model model) {
        ItemDto findItemDto = itemQueryRepository.findItemDtoById(itemId);
        model.addAttribute("itemDto", findItemDto);
        /* ↓ 이전 페이지로 갈 수 있게 끔 구현하기 위해 pageNumeber 를 파라미터로 받아서 model 로 넘김. */
        model.addAttribute("page", pageNumber);
        return "item/product";
    }

}
