package org.example.internet_shop.controller;


import lombok.extern.slf4j.Slf4j;
import org.example.internet_shop.Entity.Image;
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
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@RequestMapping("/image")
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
            return getPlaceholderImage();
        } catch (InvalidImageException e) {
            log.error("Invalid image, id={}", id);
            return getPlaceholderImage();
        } catch (Exception e) {
            log.error("Unexpected exception, image id={}", id, e);
            return getPlaceholderImage();
        }
    }
        //!!!!!!!РАВЗОБРАТЬ!!!!!!!!!!!
    private ResponseEntity<InputStreamResource> createImageResponse(Image image) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.inline()
                        .filename(image.getOriginalFileName())
                        .build());
        headers.setContentType(MediaType.valueOf(image.getContentType()));
        headers.setContentLength(image.getSize());
        headers.setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS));

        InputStreamResource resource = new InputStreamResource(
                new ByteArrayInputStream(image.getBytes()));

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    // Метод для возврата placeholder изображения
    private ResponseEntity<InputStreamResource> getPlaceholderImage() {
        try {
            // Создаем простой placeholder
            String svg = "<svg width=\"200\" height=\"200\" xmlns=\"http://www.w3.org/2000/svg\">" +
                    "<rect width=\"100%\" height=\"100%\" fill=\"#f0f0f0\"/>" +
                    "<text x=\"50%\" y=\"50%\" font-family=\"Arial\" font-size=\"14\" fill=\"#666\" text-anchor=\"middle\" dy=\".3em\">No Image</text>" +
                    "</svg>";

            byte[] bytes = svg.getBytes(StandardCharsets.UTF_8);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf("image/svg+xml"));
            headers.setContentLength(bytes.length);

            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(bytes));
            return ResponseEntity.ok().headers(headers).body(resource);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
