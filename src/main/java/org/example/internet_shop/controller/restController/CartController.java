package org.example.internet_shop.controller.restController;

import org.example.internet_shop.Entity.MyUser;
import org.example.internet_shop.dto.*;
import org.example.internet_shop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/get")
    public ResponseEntity<CartDto> getCart(@AuthenticationPrincipal MyUser user) {
        CartDto cart = cartService.getCart(user);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestBody AddToCartRequest request,
            @AuthenticationPrincipal MyUser user) {

        // Проверяем, что пользователь аутентифицирован
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Пользователь не аутентифицирован");
        }

        try {
            CartDto cart = cartService.addToCart(request, user);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при добавлении в корзину: " + e.getMessage());
        }
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