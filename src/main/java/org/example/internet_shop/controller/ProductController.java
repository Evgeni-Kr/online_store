package org.example.internet_shop.controller;


import org.example.internet_shop.Entity.Product;
import org.example.internet_shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
            System.out.println("Found products: " + products.size());
        } catch (Exception e) {
            System.out.println("Error getting products: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("products", new ArrayList<Product>());
        }
        return "home_page";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        return "addProductForm";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public String createProduct(
            @RequestParam("files") List<MultipartFile> files,
            @ModelAttribute Product product) throws IOException {
        productService.saveProduct(product, files);
        return "redirect:/product/get/products"; // Редирект на список товаров
    }

    @GetMapping("/get/{id}")
    public String getProduct(@PathVariable Long id, Model model) {
        Product product = productService.findProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        model.addAttribute("product", product);
        return "productDetails";
    }
}
