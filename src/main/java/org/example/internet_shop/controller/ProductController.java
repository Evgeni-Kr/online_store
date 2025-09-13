package org.example.internet_shop.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductController {


    @GetMapping("/get/product/{id}")
    public String getProductById(@PathVariable int id, Model model) {
        return "redirect:/products/" + id;
    }

    @GetMapping("/get/product")
    public String getProducts(Model model) {
        return "redirect:/products";
    }
}
