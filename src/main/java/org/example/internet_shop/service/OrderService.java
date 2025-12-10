package org.example.internet_shop.service;

import lombok.extern.slf4j.Slf4j;
import org.example.internet_shop.Entity.*;
import org.example.internet_shop.dto.OrderDto;
import org.example.internet_shop.repository.OrderRepository;
import org.example.internet_shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {
    private final OrderRepository repository;
    private final CartService cartService;
    private final ProductRepository productRepository;

    @Autowired
    public OrderService(OrderRepository repository, CartService cartService, ProductRepository productRepository) {
        this.cartService = cartService;
        this.repository = repository;
        this.productRepository = productRepository;
    }

    public List<Order> findAll() {
        return repository.findAll();
    }

    @Transactional
    public OrderDto createOrderFromCart(MyUser user) {
        Cart cart = cartService.getOrCreateCartForUser(user);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Корзина пуста");
        }
        Order order = createOrderFromCart(cart);

        updateProductStock(cart);
        cartService.clearCart(user);

        return new OrderDto(order);
    }

    private Order createOrderFromCart(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setStatus(STATUS.processed);
        order.setCreatedDate(LocalDateTime.now());
        List<OrderItem> orderItems = cart.getItems()
                .stream()
                .map(item->{
                    OrderItem orderItem = item.toOrderItem();
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .collect(Collectors.toList());
            order.setItems(orderItems);
            return order;
    }
    private void updateProductStock(Cart cart) {
        cart.getItems().forEach(cartItem -> {
            Product product = cartItem.getProduct();
            int newStock = product.getStockQuantity() - cartItem.getQuantity();
            product.setStockQuantity(Math.max(0, newStock));
            productRepository.save(product);
        });
    }

}
