package org.example.internet_shop.dto;

import lombok.Data;
import org.example.internet_shop.Entity.OrderItem;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private Long id;
    private Long productId;
    private String productName;
    private String productDescription;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalPrice;
    private String productImageUrl;

    public OrderItemDto(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.productId = orderItem.getProduct().getId();
        this.productName = orderItem.getProduct().getName();
        this.productDescription = orderItem.getProduct().getDescription();
        this.price = BigDecimal.valueOf(orderItem.getPrice());
        this.quantity = orderItem.getQuantity();
        this.totalPrice = this.price.multiply(BigDecimal.valueOf(this.quantity));

        // Если у продукта есть изображение
        /*if (orderItem.getProduct().getImageUrl() != null) {
            this.productImageUrl = orderItem.getProduct().getImageUrl();
        }*/
    }
}