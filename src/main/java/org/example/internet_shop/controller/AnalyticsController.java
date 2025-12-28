package org.example.internet_shop.controller;

import lombok.RequiredArgsConstructor;
import org.example.internet_shop.service.ForecastService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final ForecastService forecastService;

    @GetMapping("/forecast/{productId}")
    public double getForecast(@PathVariable Long productId,
                              @RequestParam(defaultValue = "7") int days) {
        return forecastService.forecastDemand(productId, days);
    }

    @GetMapping("/stock/{productId}")
    public double getRecommendedStock(@PathVariable Long productId,
                                      @RequestParam(defaultValue = "7") int days,
                                      @RequestParam(defaultValue = "1.3") double safetyCoef) {
        return forecastService.recommendedStock(productId, days, safetyCoef);
    }
}
