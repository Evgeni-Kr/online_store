package org.example.internet_shop.controller;

import org.example.internet_shop.Entity.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @GetMapping("/getOrders")
    public String getAllOrders(Model model) {
        List<Order> orders;
        return "orderPage";
    }
}
