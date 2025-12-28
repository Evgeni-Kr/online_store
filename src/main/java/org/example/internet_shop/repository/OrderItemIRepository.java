package org.example.internet_shop.repository;

import org.example.internet_shop.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderItemIRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT oi FROM OrderItem oi " +
            "JOIN oi.order o " +
            "WHERE oi.product.id = :productId AND o.createdDate BETWEEN :from AND :to")
    List<OrderItem> findSalesByProductAndPeriod(Long productId,
                                                LocalDateTime from,
                                                LocalDateTime to);
}
