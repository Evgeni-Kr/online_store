package org.example.internet_shop.service;

import lombok.extern.slf4j.Slf4j;
import org.example.internet_shop.Entity.Image;
import org.example.internet_shop.Entity.Product;
import org.example.internet_shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAllProducts() {
        try {
            List<Product> products = productRepository.findAll();

            // Убедимся, что у каждого продукта есть previewImageId
            for (Product product : products) {
                if (product.getPreviewImageId() == null && !product.getImages().isEmpty()) {
                    // Находим первое preview изображение или первое изображение
                    Image previewImage = product.getImages().stream()
                            .filter(Image::isPreviewImage)
                            .findFirst()
                            .orElse(product.getImages().getFirst());
                    product.setPreviewImageId(previewImage.getId());
                }
            }

            log.info("Found {} products", products.size());
            return products;
        } catch (Exception e) {
            log.error("Error finding products", e);
            return new ArrayList<>();
        }
    }


    @Transactional
    public void saveProduct(Product product, List<MultipartFile> files) {
        try {
            // Ваша логика сохранения
            boolean isFirst = true;
            for (MultipartFile file : files) {
                if (!file.isEmpty() && file.getSize() > 0) {
                    Image image = toImageEntity(file);
                    if (isFirst) {
                        image.setPreviewImage(true);
                        isFirst = false;
                    }
                    product.addImageToProduct(image);
                }
            }

            Product savedProduct = productRepository.save(product);

            // Установка previewImageId
            if (!savedProduct.getImages().isEmpty()) {
                Image previewImage = savedProduct.getImages().stream()
                        .filter(Image::isPreviewImage)
                        .findFirst()
                        .orElse(savedProduct.getImages().getFirst());
                savedProduct.setPreviewImageId(previewImage.getId());
                productRepository.save(savedProduct);
            }

            log.info("Product saved successfully with ID: {}", savedProduct.getId());
        } catch (Exception e) {
            log.error("Error saving product", e);
            throw new RuntimeException("Failed to save product", e);
        }
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getOriginalFilename());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    public Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }
    @Transactional
    public List<Product> findProductByCategory(String category) {
        try {
            return productRepository.findAllByCategory(category);
        }catch (NullPointerException e) {
            log.error("Error finding products", e);
            return new ArrayList<>();
        }

    }
    @Transactional
    public List<Product> findProductByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return productRepository.findAllIfNameContain(name.toLowerCase().trim());
    }
}