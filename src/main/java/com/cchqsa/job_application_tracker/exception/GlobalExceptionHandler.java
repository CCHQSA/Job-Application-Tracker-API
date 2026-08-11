package com.cchqsa.job_application_tracker.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final HttpServletRequest request;

    public GlobalExceptionHandler(HttpServletRequest request) {
        this.request = request;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {

        LocalDateTime timeStamp = LocalDateTime.now();
        int httpStatus = HttpStatus.NOT_FOUND.value();
        String httpReason = HttpStatus.NOT_FOUND.getReasonPhrase();
        String eMessage = ex.getMessage();

        ErrorResponse errorResponse = new ErrorResponse(
                timeStamp,
                httpStatus,
                httpReason,
                eMessage,
                request.getRequestURI()
                );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
