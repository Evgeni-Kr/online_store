package org.example.internet_shop.security.provisioning;

import org.example.internet_shop.service.MyCustomUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.HashMap;
import java.util.Map;


public class CustomInMemoryUserDetailsManager extends InMemoryUserDetailsManager {


    private final Map<String, UserDetails> inMemoryUsers = new HashMap<>();




    public CustomInMemoryUserDetailsManager() {
       super();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Сначала ищем в памяти
        UserDetails inMemoryUser = inMemoryUsers.get(username.toLowerCase());
        if (inMemoryUser != null) {
            return inMemoryUser;
        }

        // Если не найден в памяти, используем родительскую реализацию
        return super.loadUserByUsername(username);
    }

    @Override
    public void createUser(UserDetails user) {
        inMemoryUsers.put(user.getUsername().toLowerCase(), user);
        super.createUser(user);
    }
}
