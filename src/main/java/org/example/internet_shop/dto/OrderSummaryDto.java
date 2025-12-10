package org.example.internet_shop.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderSummaryDto {
    private Long id;
    private String orderNumber;
    private LocalDateTime orderDate;
    private BigDecimal totalPrice;
    private String status;
    private int totalItems;
}