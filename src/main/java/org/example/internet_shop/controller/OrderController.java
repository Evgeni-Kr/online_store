package org.example.internet_shop.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.internet_shop.Entity.MyUser;
import org.example.internet_shop.dto.OrderDto;
import org.example.internet_shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/get")
    public String getUserOrders(Model model, @AuthenticationPrincipal MyUser user) {
        if (user == null) {
            return "redirect:/api/login";
        }

        try {
            // Нужно добавить метод в OrderService для получения заказов пользователя
            List<OrderDto> orders = orderService.getUserOrders(user);
            model.addAttribute("orders", orders);
            log.info("Loaded {} orders for user {}", orders.size(), user.getUsername());
        } catch (Exception e) {
            log.error("Error loading orders for user {}", user.getUsername(), e);
            model.addAttribute("error", "Ошибка при загрузке заказов");
        }

        return "orderPage";
    }

    @GetMapping("/details/{id}")
    public String getOrderDetails(@PathVariable Long id,
                                  @AuthenticationPrincipal MyUser user,
                                  Model model) {
        if (user == null) {
            return "redirect:/api/login";
        }

        try {
            OrderDto order = orderService.getOrderById(id, user);
            model.addAttribute("order", order);
            return "orderDetails";
        } catch (Exception e) {
            log.error("Error loading order details for order {}", id, e);
            return "redirect:/order/get";
        }
    }
}