package com.api.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
@Data
public class ErrorResponseDTO {
    int status;
    String message;
    String path;
    String error;
    LocalDateTime timestamp;

    public static ErrorResponseDTO buildError(HttpStatus status, String message, HttpServletRequest request) {
        return ErrorResponseDTO.builder()
                .status(status.value())
                .message(message)
                .path(request.getRequestURI())
                .error(status.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .build();
    }
}

