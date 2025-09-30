package org.example.internet_shop.service;

import org.example.internet_shop.dto.Product;
import org.example.internet_shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }



    public Product addProduct(Product product) {
        productRepository.save(product);
        return product;
    }

    public void deleteProductById(int id) {
        productRepository.deleteById(id);
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> findProductById(int id) {
        return productRepository.findById(id);
    }
}
