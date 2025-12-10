package org.example.internet_shop.exceptions;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Обработка 404 ошибок
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNotFound(NoHandlerFoundException ex, Model model) {
        logger.error("404 error: {}", ex.getRequestURL());
        model.addAttribute("errorMessage", "Страница не найдена");
        model.addAttribute("errorCode", "404");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage()
                + " \nПричина: " + ex.getCause()
                + " Класс: " + ex.getClass());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleAllExceptions(HttpServletRequest request, Exception ex) {
        logger.error("Ошибка: ", ex);
        logger.error("Произошла ошибка при обработке запроса: {}", request.getRequestURI(), ex);
        // Формируем информативное сообщение
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("Сообщение: ").append(ex.getMessage()).append("\n");
        errorMessage.append("Класс исключения: ").append(ex.getClass().getName()).append("\n");

        if (ex.getCause() != null) {
            errorMessage.append("Причина: ").append(ex.getCause()).append("\n");
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorMessage.toString());
    }

    // Обработка ошибок доступа
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage()
                + " \nПричина: " + ex.getCause()
                + " Класс: " + ex.getClass());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleBadInput(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cause.getMessage());
    }
}