package org.example.internet_shop.controller;

import org.example.internet_shop.Entity.Cart;
import org.example.internet_shop.Entity.MyUser;
import org.example.internet_shop.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/shopCart")
public class ShopCartController {

    private static final Logger logger = LoggerFactory.getLogger(ShopCartController.class);
    private final CartService cartService;

    @Autowired
    public ShopCartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/get")
    public String showCart(Model model, @AuthenticationPrincipal MyUser user) {
        if (user == null) {
            return "redirect:/login";
        }

        try {
            Cart cart = cartService.getOrCreateCartForUser(user);
            model.addAttribute("cart", cart);
            return "shopCart";
        } catch (Exception e) {
            model.addAttribute("error", "Не удалось загрузить корзину");
            return "shopCart";
        }
    }
    @PostMapping("/update/{productId}")
    public String updateItemQuantity(
            @PathVariable Long productId,
            @RequestParam Integer quantity,
            @AuthenticationPrincipal MyUser user) {

        if (user == null) {
            return "redirect:/login";
        }

        cartService.updateCartItemQuantity(productId, quantity, user);
        return "redirect:/shopCart/get";
    }

    @PostMapping("/remove/{productId}")
    public String removeItem(
            @PathVariable Long productId,
            @AuthenticationPrincipal MyUser user) {

        logger.info("=== УДАЛЕНИЕ ТОВАРА ===");
        logger.info("ID товара: {}", productId);
        logger.info("Пользователь: {}", user != null ? user.getUsername() : "null");
        logger.info("ID пользователя: {}", user != null ? user.getId() : "null");

        if (user == null) {
            logger.error("Пользователь не авторизован");
            return "redirect:/login";
        }

        try {
            // Проверяем, что товар существует перед удалением
            Cart cart = cartService.getOrCreateCartForUser(user);
            logger.info("Корзина пользователя: {}", cart.getId());
            logger.info("Товаров в корзине до удаления: {}", cart.getItems().size());

            // Проверяем, есть ли товар в корзине
            boolean hasProduct = cart.getItems().stream()
                    .anyMatch(item -> item.getProduct().getId().equals(productId));
            logger.info("Товар {} найден в корзине: {}", productId, hasProduct);

            // Вызываем удаление
            cartService.removeFromCart(productId, user);
            logger.info("Товар удален (вызов сервиса завершен)");

            // Проверяем результат
            cart = cartService.getOrCreateCartForUser(user);
            logger.info("Товаров в корзине после удаления: {}", cart.getItems().size());

        } catch (Exception e) {
            logger.error("Ошибка при удалении товара: ", e);
        }

        return "redirect:/shopCart/get";
    }

}
