package org.example.internet_shop.controller;

import org.example.internet_shop.Entity.Product;
import org.example.internet_shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {


       private final ProductService productService;
       @Autowired
       public AdminRestController(ProductService productService) {
           this.productService = productService;
       }



    @GetMapping("/get/product/{id}")
    public Optional<Product> getAdminProduct(@PathVariable Long id) {
        return productService.findProductById(id);
    }

    @GetMapping("/get/products")
    public List<Product> getAllAdminProducts() {
        return productService.findAllProducts();
    }




    @DeleteMapping("/delete/product/{id}")
    public String deleteAdminProduct(@PathVariable Long id) {
           Optional<Product> product = productService.findProductById(id);
        productService.deleteProductById(id);
        return "Предмет "  + product.toString() + " удалён";
    }


}
