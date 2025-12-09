package org.example.internet_shop.repository;

import org.example.internet_shop.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemIRepository extends JpaRepository<OrderItem, Long> {
}
