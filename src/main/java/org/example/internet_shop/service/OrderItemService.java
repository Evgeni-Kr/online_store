package org.example.internet_shop.service;

import org.example.internet_shop.repository.OrderItemIRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService {

    private final OrderItemIRepository repository;
    @Autowired
    public OrderItemService(OrderItemIRepository repository) {
        this.repository = repository;
    }


}
