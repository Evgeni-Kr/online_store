package org.example.internet_shop.repository;

import org.example.internet_shop.dao.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    public Product findProductById(int id);
}

