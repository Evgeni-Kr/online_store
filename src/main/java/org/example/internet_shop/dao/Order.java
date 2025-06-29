package org.example.internet_shop.dao;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @ManyToOne
    @JoinColumn(name="user_id")
    private MyUser user;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;
    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private STATUS status;
}

enum STATUS {
    processed,
    on_the_way,
    delivered
}