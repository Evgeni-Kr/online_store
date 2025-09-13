package org.example.internet_shop.dao;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;

    @Column(name="quantity")
    private int quantity;
    @Column(name="price")
    private double price;
}
