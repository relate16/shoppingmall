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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor
public class ProductsController {

    private final ItemQueryRepository itemQueryRepository;

    @GetMapping("/products")
    public String productsGet(@ModelAttribute ItemDto itemDto, Model model, Pageable pageable) {

        Page<ItemDto> page = itemQueryRepository.showPageList(pageable);
        model.addAttribute("page", page);

        return "products";
    }

    @GetMapping("/products/detail")
    public String productGet(@RequestParam(value = "id") Long itemId, @RequestParam(value = "page") int pageNumber, Model model) {
        ItemDto findItemDto = itemQueryRepository.findItemDtoById(itemId);
        model.addAttribute("itemDto", findItemDto);
        //↓ 이전 페이지로 갈 수 있게 끔 구현하기 위해 pageNumeber를 파라미터로 받아서 model로 넘김.
        Model modelToString = model.addAttribute("page", pageNumber);
        return "product";
    }

    //↓ addCart 부분 미구현, 로그인 방식 등을 배우고 구현해야 주먹구구식으로 구현하지 않을 것 같아 구현하지 않음.
    @PostMapping("/products/detail")
    public String addCart(@RequestParam(value = "id") Long itemId, @RequestParam(value = "page") int pageNumber, Model model, RedirectAttributes redirectAttributes) {


        /* addCart 이후 Back눌렀을 때, 전에 있었던 페이지 번호 유지하기 위해 임시방편으로 넣어서 둠.
         204 No Content를 활용해도 될 것 같기도 한데 활용 방법 아직 안배워서 미구현 */
        redirectAttributes.addAttribute("id", itemId);
        redirectAttributes.addAttribute("page", pageNumber);

        return "redirect:/products/detail";
    }



}
