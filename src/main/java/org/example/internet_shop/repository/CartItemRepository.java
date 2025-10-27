package org.example.internet_shop.repository;

import org.example.internet_shop.Entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
