package org.example.internet_shop.service;

import org.example.internet_shop.dto.Image;
import org.example.internet_shop.dto.Product;
import org.example.internet_shop.exceptions.ImageProcessingException;
import org.example.internet_shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Transactional
    public void saveProduct(Product product, List<MultipartFile> files) {
        validateProductAndFiles(product, files);

        boolean isFirst = true;
        for (MultipartFile file : files) {
            if (isValidImageFile(file)) {
                Image image = toImageEntity(file);
                if (isFirst) {
                    image.setPreviewImage(true);
                    isFirst = false;
                }
                product.addImageToProduct(image);
            }
        }

        // Сохраняем один раз
        Product savedProduct = productRepository.save(product);

        // Безопасно устанавливаем previewImageId
        setPreviewImageId(savedProduct);
    }

    private void validateProductAndFiles(Product product, List<MultipartFile> files) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (files == null) {
            throw new IllegalArgumentException("Files list cannot be null");
        }
    }

    private boolean isValidImageFile(MultipartFile file) {
        return file != null &&
                !file.isEmpty() &&
                file.getSize() > 0 &&
                file.getContentType() != null &&
                file.getContentType().startsWith("image/");
    }

    private Image toImageEntity(MultipartFile file) {
        try {
            Image image = new Image();
            image.setName(generateImageName(file.getOriginalFilename())); // Исправлено!
            image.setOriginalFileName(file.getOriginalFilename());
            image.setSize(file.getSize());
            image.setContentType(file.getContentType());
            image.setBytes(file.getBytes());
            return image;
        } catch (IOException e) {
            throw new ImageProcessingException("Failed to process image: " +
                    file.getOriginalFilename(), e);
        }
    }
    public void deleteProductById(int id) {
        productRepository.deleteById(id);
    }
    private void setPreviewImageId(Product product) {
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            Long previewImageId = product.getImages().stream()
                    .filter(Image::isPreviewImage)
                    .findFirst()
                    .map(Image::getId)
                    .orElse(product.getImages().get(0).getId()); // Резервный вариант

            product.setPreviewImageId(previewImageId);
            // Не нужно сохранять снова - изменения в транзакции
        }
    }

    // Генерация имени файла
    private String generateImageName(String originalFileName) {
        if (originalFileName == null) {
            return "image_" + System.currentTimeMillis();
        }
        // Убираем расширение для имени
        String nameWithoutExtension = originalFileName.replaceFirst("[.][^.]+$", "");
        return nameWithoutExtension + "_" + System.currentTimeMillis();
    }
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> findProductById(int id) {
        return productRepository.findById(id);
    }
}
