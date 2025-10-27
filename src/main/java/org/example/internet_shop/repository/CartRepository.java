package org.example.internet_shop.repository;

import org.example.internet_shop.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CartRepository  extends JpaRepository<Cart, Long> {
}
