package org.example.internet_shop.controller;

import org.example.internet_shop.Entity.MyUser;
import org.example.internet_shop.dto.*;
import org.example.internet_shop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartDto> getCart(@AuthenticationPrincipal MyUser user) {
        CartDto cart = cartService.getCart(user);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<CartDto> addToCart(
            @RequestBody AddToCartRequest request,
            @AuthenticationPrincipal MyUser user) {
        CartDto cart = cartService.addToCart(request, user);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/item/{itemId}")
    public ResponseEntity<CartDto> updateCartItem(
            @PathVariable Long itemId,
            @RequestParam Integer quantity,
            @AuthenticationPrincipal MyUser user) {
        CartDto cart = cartService.updateCartItem(itemId, quantity, user);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/item/{productId}")
    public ResponseEntity<CartDto> removeFromCart(
            @PathVariable Long productId,
            @AuthenticationPrincipal MyUser user) {
        CartDto cart = cartService.removeFromCart(productId, user);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal MyUser user) {
        cartService.clearCart(user);
        return ResponseEntity.ok().build();
    }
}