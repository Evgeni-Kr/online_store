package org.example.internet_shop.dto;


import lombok.Data;
import org.example.internet_shop.Entity.Cart;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CartDto {
    private Long id;
    private Long userId;
    private List<CartItemDto> items;
    private BigDecimal totalPrice;
    private int totalItems;

    public CartDto(Cart cart) {
        this.id = cart.getId();
        this.userId = cart.getUser().getId();
        this.items = cart.getItems().stream()
                .map(CartItemDto::new)
                .collect(Collectors.toList());
        this.totalPrice = cart.getTotalPrice();
        this.totalItems = cart.getTotalItems();
    }
}