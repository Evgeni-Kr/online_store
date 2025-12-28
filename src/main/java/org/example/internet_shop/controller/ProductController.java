package org.example.internet_shop.controller;


import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
            model.addAttribute("searchQuery", "");
            String message = "Количество продуктов"+products.size();
            log.info(message);
        } catch (Exception e) {
            log.info(e.getMessage());
            e.getStackTrace();
            model.addAttribute("products", new ArrayList<Product>());
            model.addAttribute("searchQuery", "");
        }
        return "home_page";
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("searchQuery", "");
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
        model.addAttribute("searchQuery", "");
        return "productDetails";
    }

    @GetMapping("category/{category}")
    public String getProductsByCategory(@PathVariable String category, Model model) {
        try {
            List<Product> products = productService.findProductByCategory(category);
            model.addAttribute("products", products);
            model.addAttribute("searchQuery", "");
            final String message = "Категория" + category;
            final String productCount = "Количество товара" + products.size();
            log.info(message);
            log.info(productCount);
        }catch(NullPointerException e) {
            model.addAttribute("products", new ArrayList<Product>());
            model.addAttribute("searchQuery", "");
        }
        return "home_page";
    }

    @GetMapping("name/")
    public String searchProducts(Model model, @RequestParam(required = false) String q) {
        List<Product> products = productService.findProductByName(q);
        model.addAttribute("products", products);
        model.addAttribute("searchQuery", q); // Добавьте это для отображения введенного запроса на странице
        return "home_page";
    }
}
