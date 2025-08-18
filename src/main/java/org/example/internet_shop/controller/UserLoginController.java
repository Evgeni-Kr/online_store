package org.example.internet_shop.controller;

import org.example.internet_shop.dao.MyUser;
import org.example.internet_shop.repository.UserRepository;
import org.example.internet_shop.service.MyUserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class UserLoginController {
    private final MyUserLoginService userLoginService;

    @Autowired
    public UserLoginController(MyUserLoginService userService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userLoginService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user",new MyUser());
        return "sign_up_page";
    }

    @PostMapping("/sign_up")
    public String signUp(Model model,
                         @ModelAttribute("user") MyUser user) {
        if(userLoginService.signUp(user)) {
            return "redirect:/api/login?registered";
        } else {
            model.addAttribute("error", "Этот пользователь уже существует");
            return "sign_up_page";
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
        return "login_page";
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("name","JAVA");
        return "index";
    }
}