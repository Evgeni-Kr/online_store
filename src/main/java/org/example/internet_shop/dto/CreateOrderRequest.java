package org.example.internet_shop.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateOrderRequest {
    private Long userId;
    private List<OrderItemRequest> items;
    private String shippingAddress;
    private String paymentMethod;

    @Data
    public static class OrderItemRequest {
        private Long productId;
        private Integer quantity;
    }
}