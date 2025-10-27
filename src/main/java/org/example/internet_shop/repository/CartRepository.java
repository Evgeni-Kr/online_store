package org.example.internet_shop.repository;

import org.example.internet_shop.Entity.Cart;
import org.example.internet_shop.Entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(MyUser user);
    Optional<Cart> findByUserId(Long userId);
}