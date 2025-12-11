package org.example.internet_shop.repository;

import org.example.internet_shop.Entity.MyUser;
import org.example.internet_shop.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Найти все заказы пользователя
    List<Order> findByUser(MyUser user);

    // Или с использованием userId
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId ORDER BY o.createdDate DESC")
    List<Order> findByUserId(@Param("userId") Long userId);

    // Найти конкретный заказ пользователя
    @Query("SELECT o FROM Order o WHERE o.id = :orderId AND o.user.id = :userId")
    Optional<Order> findByIdAndUserId(@Param("orderId") Long orderId, @Param("userId") Long userId);
}