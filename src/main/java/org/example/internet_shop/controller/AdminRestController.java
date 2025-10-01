package org.example.internet_shop.controller;

import org.example.internet_shop.dto.Product;
import org.example.internet_shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/rest")
public class AdminRestController {


       private final ProductService productService;
       @Autowired
       public AdminRestController(ProductService productService) {
           this.productService = productService;
       }



    @GetMapping("/get/product/{id}")
    public Optional<Product> getAdminProduct(@PathVariable int id, Model model) {
        return productService.findProductById(id);
    }

    @GetMapping("/get/products")
    public List<Product> getAllAdminProducts(Model model) {
        return productService.findAllProducts();
    }




    @DeleteMapping("/delete/product/{id}")
    public String deleteAdminProduct(@PathVariable int id) {
           Optional<Product> product = productService.findProductById(id);
        productService.deleteProductById(id);
        return "Предмет "  + product.toString() + " удалён";
    }


}
