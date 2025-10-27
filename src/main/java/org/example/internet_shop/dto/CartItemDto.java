package org.example.internet_shop.dto;

import lombok.Data;
import org.example.internet_shop.Entity.CartItem;

@Data
public class CartItemDto {
    private Long id;
    private Long productId;
    private String productName;
    private double productPrice;
    private Integer quantity;
    private double totalPrice;
    private String previewImageUrl;

    public CartItemDto(CartItem cartItem) {
        this.id = cartItem.getId();
        this.productId = cartItem.getProduct().getId();
        this.productName = cartItem.getProduct().getName();
        this.productPrice = cartItem.getProduct().getPrice();
        this.quantity = cartItem.getQuantity();
        this.totalPrice = cartItem.getTotalPrice();
        // Здесь можно добавить логику для получения URL превью изображения
    }
}