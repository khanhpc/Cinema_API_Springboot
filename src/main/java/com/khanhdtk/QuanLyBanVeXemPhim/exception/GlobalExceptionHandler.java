package com.khanhdtk.QuanLyBanVeXemPhim.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false));
    }

    // Bắt lỗi BadRequestException
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage handleBadRequestException(BadRequestException ex, WebRequest request) {
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false));
    }

    // Bắt tất cả các lỗi hệ thống khác (Lỗi 500)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleGlobalException(Exception ex, WebRequest request) {
        return new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                "Lỗi hệ thống rồi bác ơi: " + ex.getMessage(),
                request.getDescription(false));
    }
}
