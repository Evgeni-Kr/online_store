package org.example.internet_shop.exceptions;

public class ImageProcessingException extends RuntimeException {
    public ImageProcessingException(String message) {
        super(message);
    }

    public ImageProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
