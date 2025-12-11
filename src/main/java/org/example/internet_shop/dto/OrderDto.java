package org.example.internet_shop.dto;

import lombok.Data;
import org.example.internet_shop.Entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private List<OrderItemDto> items;
    private String status;
    private BigDecimal totalPrice;
    private LocalDateTime orderDate;

    public OrderDto(Order order) {
        this.id = order.getId();
        this.userId = order.getUser().getId();
        this.userName = order.getUser().getUsername();
        this.status = order.getStatusName();
        if (order.getCreatedDate() != null) {
            this.orderDate = order.getCreatedDate();
        } else {
            System.err.println("WARNING: createdDate is null for order ID: " + order.getId());
            this.orderDate = LocalDateTime.now();
        }
        this.items = order.getItems().stream()
                .map(OrderItemDto::new)
                .toList();

        this.totalPrice = items.stream()
                .map(OrderItemDto::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}