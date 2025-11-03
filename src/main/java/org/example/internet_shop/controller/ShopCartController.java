package org.example.internet_shop.controller;

import org.example.internet_shop.Entity.Cart;
import org.example.internet_shop.Entity.MyUser;
import org.example.internet_shop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shopCart")
public class ShopCartController {

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
}
