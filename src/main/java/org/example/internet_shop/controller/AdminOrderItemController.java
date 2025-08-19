package org.example.internet_shop.controller;

import org.example.internet_shop.dao.OrderItem;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin")
public class AdminOrderItemController {


    @GetMapping("/get/product/{id}")
    public String getAdminOrderItem(@PathVariable int id, Model model) {

      return "";
    }

    @GetMapping("/get/products")
    public String getAllAdminOrderItems(Model model) {
        return "";
    }

    @PostMapping("/post/product")
    public String postAdminOrderItem(@ModelAttribute OrderItem orderItem, Model model) {
        return "";
    }

    @PutMapping("/put/product")
    public String putAdminOrderItem(@ModelAttribute OrderItem orderItem, Model model) {
        return "";
    }

    @DeleteMapping("/delete/product/{id}")
    public String deleteAdminOrderItem(@PathVariable int id , Model model) {
        return "";
    }

}
