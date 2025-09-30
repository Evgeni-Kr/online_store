package org.example.internet_shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/product")
public class AdminProductController {

    @GetMapping("/addForm")
    public String getAdminAddProductForm() {
        return "add_product_form";
    }
}
