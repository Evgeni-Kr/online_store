package org.example.internet_shop.repository;

import org.example.internet_shop.dao.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderItem, Integer> {
}
