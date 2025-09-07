package com.api.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
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

import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Map<String, String> SORT_FIELDS = Map.of(
            "ClientController", "id, name, surname, emailAddress, phoneNumber",
            "ProductController", "id, name, description, price",
            "OrderController", "id, creationDate, status",
            "EmployeeController", "id, name, surname, emailAddress, position"
    );

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFoundException(
            EntityNotFoundException e,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseDTO.buildError(
                        HttpStatus.NOT_FOUND,
                        "Не найдено: " + e.getMessage(),
                        request));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponseDTO> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e,
            HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ValidationErrorResponseDTO.buildError(
                        HttpStatus.BAD_REQUEST,
                        "Ошибка валидации",
                        errors,
                        request
                ));
    }

    @ExceptionHandler({
            ConstraintViolationException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class
    })
    public ResponseEntity<ValidationErrorResponseDTO> handleRequestParamValidation(
            Exception ex,
            HttpServletRequest request
    ) {
        String msg = ex.getMessage();
        String customMsg = "Ошибка валидации: " + msg;
        Map<String, String> fieldErrors = new HashMap<>();

        for (var entry : SORT_FIELDS.entrySet()) {
            if (msg != null && msg.contains("sortBy") && msg.contains(entry.getKey())) {
                customMsg = "Некорректный параметр сортировки sortBy. Допустимые значения: " + entry.getValue();
                fieldErrors.put("sortBy", customMsg);
            }
        }

        return ResponseEntity.badRequest().body(
                ValidationErrorResponseDTO.buildError(
                        HttpStatus.BAD_REQUEST,
                        customMsg,
                        fieldErrors,
                        request
                )
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDTO> handleResponseStatusException(
            ResponseStatusException e, HttpServletRequest request) {
        return ResponseEntity.status(e.getStatusCode())
                .body(ErrorResponseDTO.buildError(
                        (HttpStatus) e.getStatusCode(),
                        e.getReason(),
                        request));
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ErrorResponseDTO> handleNumberFormat(
            NumberFormatException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDTO.buildError(
                        HttpStatus.BAD_REQUEST,
                        "Неверный формат цены",
                        request));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException ife && ife.getTargetType().isEnum()) {
            Class<?> enumClass = ife.getTargetType();
            String allowed = Arrays.stream(enumClass.getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));

            String message = "Некорректное значение для поля "
                    + getFieldFromPath(ife)
                    + ". Возможные значения: " + allowed;

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponseDTO.buildError(
                            HttpStatus.BAD_REQUEST,
                            message,
                            request));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDTO.buildError(
                        HttpStatus.BAD_REQUEST,
                        "Ошибка в запросе: " + ex.getMessage(),
                        request));
    }

    private String getFieldFromPath(InvalidFormatException ife) {
        return ife.getPath().isEmpty()
                ? "unknown"
                : ife.getPath().get(ife.getPath().size() - 1).getFieldName();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleOther(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponseDTO.buildError(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Что-то пошло не так: " + e.getMessage(),
                        request));
    }

}
