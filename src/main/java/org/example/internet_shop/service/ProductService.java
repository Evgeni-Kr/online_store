package org.example.internet_shop.service;

import lombok.extern.slf4j.Slf4j;
import org.example.internet_shop.dto.Image;
import org.example.internet_shop.dto.Product;
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
                        .orElse(savedProduct.getImages().get(0));
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

    public Optional<Product> findProductById(int id) {
        return productRepository.findById(id);
    }

    public void deleteProductById(int id) {
        productRepository.deleteById(id);
    }
}