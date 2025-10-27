package org.example.internet_shop.service;

import org.example.internet_shop.Entity.Cart;
import org.example.internet_shop.Entity.CartItem;
import org.example.internet_shop.Entity.MyUser;
import org.example.internet_shop.Entity.Product;
import org.example.internet_shop.dto.*;
import org.example.internet_shop.repository.CartItemRepository;
import org.example.internet_shop.repository.CartRepository;
import org.example.internet_shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final MyUserService userService;

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
            cart.addItem(newItem);
            cartRepository.save(cart);
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
        Cart cart = getOrCreateCartForUser(user);
        cartItemRepository.deleteByCartIdAndProductId(cart.getId(), productId);

        // Обновляем корзину
        cart = cartRepository.findById(cart.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

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
}