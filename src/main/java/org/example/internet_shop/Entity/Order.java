package org.example.internet_shop.Entity;

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
    @JoinColumn(name="user_id")  //покупатель
    private MyUser user;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items; // список товаров в корзине
    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private STATUS status;
}

enum STATUS {
    processed,
    on_the_way,
    delivered
}  // статус заказа