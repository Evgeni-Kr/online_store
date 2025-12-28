package org.example.internet_shop.service;


import org.example.internet_shop.Entity.OrderItem;
import org.example.internet_shop.repository.OrderItemIRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ForecastService {

     private final OrderItemIRepository orderItemRepository;

     @Autowired
     public  ForecastService(OrderItemIRepository orderItemIRepository ) {
         this.orderItemRepository = orderItemIRepository;
     }
    /**
     * Прогноз продаж на основе скользящего среднего
     */
    public double forecastDemand(Long productId, int daysWindow) {

        LocalDateTime to = LocalDateTime.now();
        LocalDateTime from = to.minusDays(daysWindow);

        List<OrderItem> sales =
                orderItemRepository.findSalesByProductAndPeriod(productId, from, to);

        if (sales.isEmpty()) return 0;

        double sum = sales.stream()
                .mapToDouble(OrderItem::getQuantity)
                .sum();

        return sum / daysWindow; // математическая модель
    }

    /**
     * Рекомендуемый складской запас
     */
    public double recommendedStock(Long productId, int daysWindow, double safetyCoef) {
        double forecast = forecastDemand(productId, daysWindow);
        return forecast * safetyCoef;
    }
}
