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
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;


    @Autowired
    public OrderService(OrderRepository orderRepository, CartService cartService, ProductRepository productRepository) {
        this.cartService = cartService;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }


    @Transactional
    public OrderDto createOrderFromCart(MyUser user) {
        Cart cart = cartService.getOrCreateCartForUser(user);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Корзина пуста");
        }
        Order order = createOrderFromCart(cart);

        updateProductStock(cart);
        orderRepository.save(order);
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

    public List<OrderDto> getUserOrders(MyUser user) {
        List<Order> orders = orderRepository.findByUser(user);
        log.info("Found {} orders for user {}", orders.size(), user.getUsername());
        return orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    // Метод для получения конкретного заказа
    public OrderDto getOrderById(Long orderId, MyUser user) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("Order not found: {}", orderId);
                    return new RuntimeException("Заказ не найден");
                });

        // Проверяем, что заказ принадлежит пользователю
        if (!order.getUser().getId().equals(user.getId())) {
            log.warn("User {} tried to access order {} belonging to user {}",
                    user.getId(), orderId, order.getUser().getId());
            throw new RuntimeException("Доступ запрещен");
        }

        return new OrderDto(order);
    }

    @Transactional
    public OrderDto createInstantOrder(MyUser user, Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));

        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Недостаточно товара на складе");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(STATUS.processed);
        order.setCreatedDate(LocalDateTime.now());

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setPrice(product.getPrice());
        item.setOrder(order);

        order.setItems(List.of(item));

        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);

        orderRepository.save(order);

        return new OrderDto(order);
    }

    public OrderDto updateStatus(Long orderId, String status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));

        STATUS newStatus;

        try {
            newStatus = STATUS.valueOf(status);
        } catch (Exception e){
            throw new IllegalArgumentException("Недопустимый статус. Доступны: processed, on_the_way, delivered");
        }

        order.setStatus(newStatus);
        orderRepository.save(order);

        return new OrderDto(order);
    }

}
