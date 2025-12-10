package org.example.internet_shop.controller;

import org.example.internet_shop.Entity.MyUser;
import org.example.internet_shop.Entity.Order;
import org.example.internet_shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @GetMapping("/get")
    public String getAllOrders(Model model, @AuthenticationPrincipal MyUser user) {
        if(user==null){
            return "redirect:/login";
        }
        List<Order> orders = orderService.findAll();
        model.addAttribute("orders", orders);
        return "orderPage";
    }
}
