package org.example.internet_shop.controller.restController;

import lombok.extern.slf4j.Slf4j;
import org.example.internet_shop.Entity.MyUser;
import org.example.internet_shop.dto.OrderDto;
import org.example.internet_shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/order")
public class OrderRestController {

    private final OrderService orderService;

    @Autowired
    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@AuthenticationPrincipal MyUser user) {
        if(user == null) {
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Пользователь не авторизован");
        }
        OrderDto order = orderService.createOrderFromCart(user);
        return ResponseEntity.ok(order);
    }
    
}
