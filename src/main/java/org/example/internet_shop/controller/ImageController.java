package org.example.internet_shop.controller;


import lombok.extern.slf4j.Slf4j;
import org.example.internet_shop.dto.Image;
import org.example.internet_shop.exceptions.ImageNotFoundException;
import org.example.internet_shop.exceptions.InvalidImageException;
import org.example.internet_shop.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<InputStreamResource> getImageById(@PathVariable Long id) {
        try {
            Image image = imageService.getImageEntity(id);
            imageService.validateImage(image);
            return createImageResponse(image);
        } catch (ImageNotFoundException e) {
            log.warn("Image not found, id={}", id);
            return ResponseEntity.notFound().build();
        } catch (InvalidImageException e) {
            log.error("Invalid image, id={}", id);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        } catch (Exception e) {
            log.error("Unexpected exception, image id={}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }
    //!!!!!!!  РАЗОБРАТЬ !!!!!!!!!
    private ResponseEntity<InputStreamResource> createImageResponse(Image image) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.inline()
                        .filename(image.getOriginalFileName())
                        .build());
        headers.setContentType(MediaType.valueOf(image.getContentType()));
        headers.setContentLength(image.getSize());
        headers.setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS)); // Кэширование

        InputStreamResource resource = new InputStreamResource(
                new ByteArrayInputStream(image.getBytes()));

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }


}
