package org.example.internet_shop.controller;

import org.example.internet_shop.Entity.MyUser;
import org.example.internet_shop.service.MyUserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class UserLoginController {
    private final MyUserLoginService userLoginService;
    private final SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();

    @Autowired
    public UserLoginController(MyUserLoginService userService) {
        this.userLoginService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user",new MyUser());
        return "signUpPage";
    }

    @PostMapping("/sign_up")
    public String signUp(Model model,
                         @ModelAttribute("user") MyUser user) {
        if(userLoginService.signUp(user)) {
            return "redirect:/api/login?registered";
        } else {
            model.addAttribute("error", "Этот пользователь уже существует");
            return "signUpPage";
        }

    }


    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        if (error != null) {
            model.addAttribute("error", "Неверные имя пользователя или пароль");
        }
        if (logout != null) {
            model.addAttribute("logout", "Вы успешно вышли");
        }
        return "loginPage";
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @GetMapping("/check-role")
    public String checkRole(Authentication authentication) {
        System.out.println("User roles: " + authentication.getAuthorities());
        return "redirect:/product/get/products";
    }
}