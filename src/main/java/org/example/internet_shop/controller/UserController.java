package org.example.internet_shop.controller;

import org.example.internet_shop.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    private  MyUserService myUserService;

    @Autowired
    public UserController(MyUserService myUserService) {
        this.myUserService = myUserService;
    }
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("name","JAVA");
        return "index";
    }
}
