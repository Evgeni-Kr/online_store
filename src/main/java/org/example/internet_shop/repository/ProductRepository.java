package org.example.internet_shop.repository;

import org.example.internet_shop.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByCategory(String category);
    @Query("select p from Product p where lower(p.name) like lower(concat('%', :name, '%'))")
    List<Product> findAllIfNameContain(@Param("name") String name);
}