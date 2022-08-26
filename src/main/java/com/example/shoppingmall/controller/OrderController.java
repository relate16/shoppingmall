package com.example.shoppingmall.controller;

import com.example.shoppingmall.constant.SessionConst;
import com.example.shoppingmall.dto.LogInDto;
import com.example.shoppingmall.dto.OrderDto;
import com.example.shoppingmall.entity.*;
import com.example.shoppingmall.exception.NotFoundException;
import com.example.shoppingmall.repository.*;
import com.example.shoppingmall.service.CartService;
import com.example.shoppingmall.service.DeliveryService;
import com.example.shoppingmall.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final MemberRepository memberRepository;
    private final DeliveryRepository deliveryRepository;
    private final DeliveryService deliveryService;
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final OrderQueryRepository orderQueryRepository;
    private final ItemRepository itemRepository;
    private final CartService cartService;

    /**
     * 상세페이지에서 상품 단건 주문시 orderForm
     */
    @GetMapping("/orders")
    public String showOrderForm(@RequestParam String username,
                                @ModelAttribute OrderDto orderDto, BindingResult bindingResult, Model model) {

        OrderDto findOrderDto = orderQueryRepository.findLastOrderDtoByUsername(username);
        findOrderDto.calculateTotalPrice();
        model.addAttribute("orderDto", findOrderDto);
        return "order/orderForm";
    }


    /**
     * 상품 상세페이지에서 주문
     */
    @PostMapping("/orders")
    public String createOrder(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) LogInDto logInDto,
                              @RequestParam Long itemId, RedirectAttributes redirectAttributes) {

        Optional<Item> itemOpt = itemRepository.findById(itemId);
        Item item = itemOpt.orElse(new Item("", 0, 0, "", ""));

        // ↓ 상세페이지에서 주문시, 수량 조절, 할인 조정하는 화면이 없으므로 quantity = 1, discount = 0 처리
        OrderItem orderItem = OrderItem.createOrderItem(item, 1, 0);
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);

        // ↓ 상세페이지에서 주문시, Delivery 따로 적는 화면이 없으므로 임의로 고정해 등록
        Address address = new Address("고정된 도시", "12345");

        Optional<Member> memberOpt = memberRepository.findByUsername(logInDto.getUsername());
        Member member = memberOpt.orElseThrow(()->new NotFoundException("해당 member를 찾을 수 없습니다."));

        Order order = orderService.createOrder(orderItems, address, member);

        redirectAttributes.addAttribute("orderId", order.getId());
        return "redirect:/orders/paymentSuccess";
    }

    /**
     * 장바구니에서 주문하기
     * 주문서만 생성하고 결제하는 건 아니라지만, 왠지 PRG 적용해야 더 좋을 것 같음.
     * 좀 더 고민이 필요함.
     */
    @PostMapping("/orders/cart")
    public String createOrderFromCart(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) LogInDto logInDto,
                                      @RequestParam(name = "id",required = false) ArrayList<Long> itemIds,
                                      @RequestParam(name = "quantity", required = false) ArrayList<Integer> quantities,
                                      Model model) {
        // ↓ 카트에 담긴 게 없을 때 주문하기 누를 경우 카트 페이지 그대로 유지.
        if (itemIds == null) {
            return "redirect:/cart";
        }

        Optional<Member> memberOpt = memberRepository.findByUsername(logInDto.getUsername());
        Member member = memberOpt.orElseThrow(()->new NotFoundException("해당 member를 찾을 수 없습니다."));

        // ↓ order 생성할 때, orderItem 을 필수가 아니게 해서 DB 에서 쓸데없는 행이 추가되지 않도록 했음
        // orderItem 을 필수값으로 설정하면 Order 생성을 위해 orderItem 을 임의로 만들어야 하고
        // orderItem을 임의로 만들면 나중에 임의로 만든 orderItem 을 따로 지우던가 해야 함.
        Order order = Order.createOrder(member, new Delivery(new Address("","")),
                null);

        for (int i = 0; i < itemIds.size(); i++) {

            Long itemId = itemIds.get(i);
            Optional<Item> itemOpt = itemRepository.findById(itemId);
            Item item = itemOpt.orElseThrow(()->new NotFoundException("해당 item을 찾을 수 없습니다."));

            //↓오더 화면에 디스카운트 처리 화면이 없어서 0 처리
            OrderItem orderItem = OrderItem.createOrderItem(item, quantities.get(i), 0);
            order.addOrderItem(orderItem);
        }

        Order saveOrder = orderRepository.save(order);

        OrderDto orderDto = orderQueryRepository.findOrderDtoByOrderId(saveOrder.getId());
        orderDto.calculateTotalPrice();

        model.addAttribute("orderDto", orderDto);
        return "order/orderForm";
    }

    @GetMapping("/orders/paymentSuccess")
    public String paymentSuccessGet(@RequestParam("orderId") Long orderId, Model model) {
        orderService.changeOrderStatus(orderId, OrderStatus.결제완료);
        model.addAttribute("orderId", orderId);
        return "paymentSuccess";
    }

    /**
     * orderDto에는 orderId, username, city, zipcode 가 들어옴
     */
    @PostMapping("/orders/paymentSuccess")
    public String paymentSuccessPost(@Validated @ModelAttribute OrderDto orderDto, BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {
        Optional<Order> orderOpt = orderRepository.findById(orderDto.getOrderId());

        //↓ orderId에 해당하는 Order 가 없을 시, 주문 페이지로 다시 이동.
        if (orderOpt.isEmpty()) {
            bindingResult.reject("notFoundOrder", "잘못된 주문Id");
            return "order/orderForm";
        }

        //↓ 입력한 배송지 order 의 delivery 에 저장.
        Order order = orderOpt.get();
        Optional<Delivery> deliveryOpt = deliveryRepository.findById(order.getDelivery().getId());
        Delivery delivery = deliveryOpt.orElse(new Delivery(new Address("", "")));

        deliveryService.changeDelivery(delivery.getId(),new Address(orderDto.getCity(), orderDto.getZipcode()));
        orderService.changeOrderStatus(order.getId(), OrderStatus.결제완료);
        cartService.cleanCart(order.getMember());

        redirectAttributes.addAttribute("orderId", orderDto.getOrderId());
        return "redirect:/orders/paymentSuccess?orderId={orderId}";
    }

    @GetMapping("/orders/ordered")
    public String orderedGet(@RequestParam("orderId") Long orderId, Model model) {
        OrderDto orderDto = orderQueryRepository.findOrderDtoByOrderId(orderId);
        model.addAttribute(orderDto);
        return "order/orderStatus";
    }

    @PostMapping("/orders/cancel")
    public String cancelOrder(@RequestParam("orderId") Long orderId, RedirectAttributes redirectAttributes) {
        orderService.cancel(orderId);
        redirectAttributes.addAttribute("orderId", orderId);
        return "redirect:/orders/ordered";
    }

    @GetMapping("/orders/showOrders")
    public String showOrders(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) LogInDto logInDto, Model model) {
        List<OrderDto> orderDtos = orderQueryRepository.findOrderDtosByUsername(logInDto.getUsername());
        for (OrderDto orderDto : orderDtos) {
            orderDto.calculateTotalPrice();
        }
        model.addAttribute("orderDtos", orderDtos);
        model.addAttribute("username", logInDto.getUsername());
        return "order/ordersView";
    }
}
