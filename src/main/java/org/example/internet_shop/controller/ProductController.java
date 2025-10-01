package org.example.internet_shop.controller;


import org.example.internet_shop.dao.Product;
import org.example.internet_shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

      private final  ProductService productService;
      @Autowired
      public ProductController(ProductService productService) {
          this.productService = productService;
      }
    @GetMapping("/get/product/{id}")
    public String getProductById(@PathVariable int id, Model model) {
        return "redirect:/products/" + id;
    }

    @GetMapping("/get/products")
    public String getProducts(Model model) {
          List<Product> products = productService.findAllProducts();
          model.addAttribute("products", products);


        return "home_page";
    }


    // Или с пагинацией
    /*@GetMapping("/products")
    public String getProductsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Page<Product> productPage = productService.getProductsPage(page, size);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        return "products";
    }*/

    @GetMapping("/create")
    public String createProduct(@RequestParam("files")List<MultipartFile> files, Product product) throws IOException
    {
        productService.saveProduct(product, files);
        return "add_product_form";
    }
}
