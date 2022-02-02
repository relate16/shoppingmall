package com.example.shoppingmall.controller;

import com.example.shoppingmall.entity.*;
import com.example.shoppingmall.dto.OrderDto;
import com.example.shoppingmall.repository.OrderItemRepository;
import com.example.shoppingmall.repository.OrderQueryRepository;
import com.example.shoppingmall.service.ItemService;
import com.example.shoppingmall.service.MemberService;
import com.example.shoppingmall.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final MemberService memberService;
    private final OrderService orderService;
    private final ItemService itemService;
    private final OrderQueryRepository orderQueryRepository;
    private final OrderItemRepository orderItemRepository;

    //로그인 기능 배우기 전이어서, 해당 member에 order를 할 수 있게 임시로 만들어 놓음.
    @GetMapping("orders/orderBefore")
    public String getOrderBefore(@RequestParam(value = "id") Long itemId, Model model) {
        model.addAttribute("itemId", itemId);
        return "orderBefore";
    }

    /**
     * PRG 적용
     */
    @PostMapping("orders/orderBefore")
    public String postOrderBefore(@RequestParam(value = "username") String username,
                                  @RequestParam(value = "itemId") Long itemId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("username", username);
        RedirectAttributes redirectAttributes1 = redirectAttributes.addAttribute("itemId", itemId);
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String showOrderForm(@RequestParam("username") String username, @RequestParam("itemId") Long itemId,
                                Model model) {
        Member member = memberService.findMemberByUsername(username);
        Item item = itemService.findItemById(itemId);

        //↓ 화면에 수량 및 discount 변경하는 게 없어서 quantity=1&discount=0처리.
        OrderItem orderItem = OrderItem.createOrderItem(item, 1, 0);

        Delivery delivery = new Delivery(member.getAddress());

        OrderDto orderDto = new OrderDto(Order.createOrder(member, delivery, orderItem),
                delivery, member, item, orderItem);

        model.addAttribute(orderDto);

        return "orderForm";
    }

    @PostMapping("/orders")
    public String createOrder(@ModelAttribute OrderDto orderDto, RedirectAttributes redirectAttributes) {
        Member member = memberService.findMemberByUsername(orderDto.getUsername());

        //주문 화면에서 city, zipcode 변경하면 Order에 변경된 값을 집어넣게 구현
        //회원 name은 변경 못하게 input 속성 readOnly 처리.
        Address address = new Address(orderDto.getCity(), orderDto.getZipcode());
        Order order = orderService.createOrderByItemIdAddressMember(orderDto.getItemId(),address, member);
        redirectAttributes.addAttribute("orderId", order.getId());
        return "redirect:/orders/paymentSuccess";
    }

    @GetMapping("/orders/paymentSuccess")
    public String paymentSuccessGet(@RequestParam("orderId") Long orderId, Model model) {
        model.addAttribute("orderId", orderId);
        return "paymentSuccess";
    }

    @GetMapping("/orders/ordered")
    public String orderedGet(@RequestParam("orderId") Long orderId, Model model) {
        OrderDto orderDto = orderQueryRepository.findOrderDtoByOrderId(orderId);
        model.addAttribute(orderDto);
        return "orderStatus";
    }

    @PostMapping("/orders/cancel")
    public String cancelOrder(@RequestParam("orderId") Long orderId, RedirectAttributes redirectAttributes) {
        orderService.cancel(orderId);
        redirectAttributes.addAttribute("orderId", orderId);
        return "redirect:/orders/ordered";
    }

    @GetMapping("/orders/showOrders/before")
    public String inputUsernameForShowOrders() {
        return "ordersViewBefore";
    }

    @PostMapping("/orders/showOrders/before")
    public String outputUsernameForShowOrders(@RequestParam("username") String username, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("username",username);
        return "redirect:/orders/showOrders";
    }

    @GetMapping("/orders/showOrders")
    public String showOrders(@RequestParam("username") String username, Model model) {
        List<OrderDto> orderDtos = orderQueryRepository.findOrderDtosByUsername(username);
        model.addAttribute("orderDtos", orderDtos);
        model.addAttribute("username", username);
        return "ordersView";

    }
}
