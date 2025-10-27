package org.example.internet_shop.service;

import lombok.extern.slf4j.Slf4j;
import org.example.internet_shop.Entity.Image;
import org.example.internet_shop.exceptions.ImageNotFoundException;
import org.example.internet_shop.exceptions.InvalidImageException;
import org.example.internet_shop.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ImageService {

    private final ImageRepository imageRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image getImageEntity(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException("Image not found with id: " + id));
    }

    // Валидация бизнес-логики
    public void validateImage(Image image) {
        if (image.getBytes() == null || image.getBytes().length == 0) {
            throw new InvalidImageException("Image data is empty");
        }
        if (!image.getContentType().startsWith("image/")) {
            throw new InvalidImageException("Invalid image content type: " + image.getContentType());
        }
    }
}
