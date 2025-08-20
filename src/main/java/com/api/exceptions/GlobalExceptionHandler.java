package com.api.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Не найдено:" + e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity
                .status(e.getStatusCode())
                .body(errors);
    }

    private static final Map<String, String> SORT_FIELDS = Map.of(
            "ClientController", "id, name, surname, emailAddress, phoneNumber",
            "ProductController", "id, name, description, price",
            "OrderController", "id, creationDate, status",
            "EmployeeController", "id, name, surname, emailAddress, position"
    );

    @ExceptionHandler({
            ConstraintViolationException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class})
    public ResponseEntity<String> handleRequestParamValidation(Exception ex) {
        String msg = ex.getMessage();
        for (var entry : SORT_FIELDS.entrySet()) {
            if (msg.contains("sortBy") && msg.contains(entry.getKey())) {
                return ResponseEntity.badRequest().body("Некорректный параметр сортировки sortBy. " +
                        "Допустимые значения: " + entry.getValue());
            }
        }
        return ResponseEntity.badRequest().body("Ошибка валидации: " + msg);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException e) {
        return ResponseEntity
                .status(e.getStatusCode())
                .body(e.getReason());
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<String> handleNumberFormat(NumberFormatException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Неверный формат цены");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException ife && ife.getTargetType().isEnum()) {
            Class<?> enumClass = ife.getTargetType();
            String fieldName = ife.getPathReference();

            String allowed = Arrays.stream(enumClass.getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));

            String message = "Некорректное значение для поля "
                    + getFieldFromPath(ife)
                    + ". Возможные значения: " + allowed;

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка в запросе: " + ex.getMessage());
    }

    private String getFieldFromPath(InvalidFormatException ife) {
        return ife.getPath().isEmpty()
                ? "unknown"
                : ife.getPath().get(ife.getPath().size() - 1).getFieldName();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOther(Exception e) {
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Что-то пошло не так: " + e.getMessage());
    }

}
