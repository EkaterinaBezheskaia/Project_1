package com.api.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Data
public class ValidationErrorResponseDTO {
    int status;
    String message;
    String error;
    Map<String, String> fieldErrors;
    String path;
    LocalDateTime timestamp;

    public static ValidationErrorResponseDTO buildError(
            HttpStatus status,
            String message,
            Map<String, String> fieldErrors,
            HttpServletRequest request
    ) {
        return ValidationErrorResponseDTO.builder()
                .status(status.value())
                .message(message)
                .error(status.getReasonPhrase())
                .fieldErrors(fieldErrors == null || fieldErrors.isEmpty() ? null : fieldErrors)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
