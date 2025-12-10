package org.example.internet_shop.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.example.internet_shop.Entity.Cart;
import org.example.internet_shop.Entity.CartItem;
import org.example.internet_shop.Entity.MyUser;
import org.example.internet_shop.Entity.Product;
import org.example.internet_shop.controller.ShopCartController;
import org.example.internet_shop.dto.*;
import org.example.internet_shop.repository.CartItemRepository;
import org.example.internet_shop.repository.CartRepository;
import org.example.internet_shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final MyUserService userService;
    private static final Logger logger = LoggerFactory.getLogger(CartService.class);
    @PersistenceContext
    private EntityManager entityManager;


    public Cart getOrCreateCartForUser(MyUser user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    @Transactional
    public CartDto addToCart(AddToCartRequest request, MyUser user) {
        Cart cart = getOrCreateCartForUser(user);
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Проверяем доступное количество
        if (product.getStockQuantity() < request.getQuantity()) {
            throw new RuntimeException("Not enough stock available");
        }

        // Ищем товар в корзине
        Optional<CartItem> existingItem = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), product.getId());

        if (existingItem.isPresent()) {
            // Обновляем количество
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            cartItemRepository.save(item);
        } else {
            // Добавляем новый товар
            CartItem newItem = new CartItem(product, request.getQuantity());
            newItem.setCart(cart);
            cart.addItem(newItem);

        }

        return new CartDto(cart);
    }

    @Transactional
    public CartDto updateCartItem(Long itemId, Integer quantity, MyUser user) {
        Cart cart = getOrCreateCartForUser(user);
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        // Проверяем, что товар принадлежит корзине пользователя
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Cart item does not belong to user's cart");
        }

        // Проверяем доступное количество
        if (item.getProduct().getStockQuantity() < quantity) {
            throw new RuntimeException("Not enough stock available");
        }

        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }

        return new CartDto(cart);
    }

    @Transactional
    public CartDto removeFromCart(Long productId, MyUser user) {
        logger.info("=== CartService.removeFromCart ===");

        // 1. Получаем корзину
        Cart cart = getOrCreateCartForUser(user);
        logger.info("Корзина ID: {}, товаров: {}", cart.getId(), cart.getItems().size());

        // 2. Находим товар в корзине
        CartItem itemToRemove = null;
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId().equals(productId)) {
                itemToRemove = item;
                logger.info("Найден товар для удаления: {}", item.getId());
                break;
            }
        }

        if (itemToRemove == null) {
            logger.error("Товар {} не найден в корзине", productId);
            throw new RuntimeException("Товар не найден в корзине");
        }

        // 3. ВАЖНО: Удаляем из коллекции items ПЕРЕД удалением из БД
        cart.getItems().remove(itemToRemove);
        logger.info("Товар удален из коллекции items. Теперь товаров: {}", cart.getItems().size());

        // 4. Удаляем из БД
        cartItemRepository.delete(itemToRemove);
        logger.info("Товар удален из БД");

        // 5. Обязательно сохраняем корзину
        cart = cartRepository.save(cart);
        logger.info("Корзина сохранена в БД");

        // 6. Явно сбрасываем кеш Hibernate для этой сущности
        entityManager.flush();
        entityManager.clear();

        return new CartDto(cart);
    }
    @Transactional
    public void clearCart(MyUser user) {
        Cart cart = getOrCreateCartForUser(user);
        cart.clear();
        cartRepository.save(cart);
    }

    public CartDto getCart(MyUser user) {
        Cart cart = getOrCreateCartForUser(user);
        return new CartDto(cart);
    }

    @Transactional
    public CartDto mergeCarts(Cart sessionCart, MyUser user) {
        Cart userCart = getOrCreateCartForUser(user);

        if (sessionCart != null && sessionCart.getItems() != null) {
            for (CartItem sessionItem : sessionCart.getItems()) {
                Optional<CartItem> existingItem = cartItemRepository
                        .findByCartIdAndProductId(userCart.getId(), sessionItem.getProduct().getId());

                if (existingItem.isPresent()) {
                    CartItem item = existingItem.get();
                    item.setQuantity(item.getQuantity() + sessionItem.getQuantity());
                    cartItemRepository.save(item);
                } else {
                    CartItem newItem = new CartItem(sessionItem.getProduct(), sessionItem.getQuantity());
                    userCart.addItem(newItem);
                }
            }
        }

        return new CartDto(cartRepository.save(userCart));
    }

    // Добавьте эти методы в ваш CartService
    @Transactional
    public void updateCartItemQuantity(Long productId, Integer quantity, MyUser user) {
        // Логика обновления количества
        Cart cart = getOrCreateCartForUser(user);
        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Товар не найден в корзине"));

        item.setQuantity(quantity);
        cartRepository.save(cart);
    }
}