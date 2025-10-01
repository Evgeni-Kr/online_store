package org.example.internet_shop.controller;


import org.example.internet_shop.dto.Product;
import org.example.internet_shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/get/products")
    public String getProducts(Model model) {
        try {
            List<Product> products = productService.findAllProducts();
            model.addAttribute("products", products);
            System.out.println("Found products: " + products.size()); // Для дебага
        } catch (Exception e) {
            System.out.println("Error getting products: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("products", new ArrayList<Product>());
        }
        return "home_page";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        return "add_product_form";
    }

    @PostMapping("/create")
    public String createProduct(
            @RequestParam("files") List<MultipartFile> files,
            @ModelAttribute Product product) throws IOException {
        productService.saveProduct(product, files);
        return "redirect:/product/get/products"; // Редирект на список товаров
    }
}
