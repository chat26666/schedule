package com.example.schedule.exception;
import com.example.schedule.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errorList = ex.getBindingResult().getFieldErrors().stream().
                map(error -> error.getField() + " : " + error.getDefaultMessage()).toList();

        ErrorResponseDto dto = ErrorResponseDto.builder()
                .timestamp(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date()))
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Validation Failed")
                .path(request.getRequestURI())
                .fieldErrors(errorList)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handlerConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        List<String> errorList = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + " : " + violation.getMessage())
                .toList();

        ErrorResponseDto dto = ErrorResponseDto.builder()
                .timestamp(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date()))
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Validation Failed")
                .path(request.getRequestURI())
                .fieldErrors(errorList)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDto> handlerResponseStatusException(ResponseStatusException ex, HttpServletRequest request) {
        ErrorResponseDto dto = ErrorResponseDto.builder()
                .timestamp(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date()))
                .status(ex.getStatusCode().value())
                .message("Access Denied")
                .path(request.getRequestURI())
                .fieldErrors(new ArrayList<>(List.of("userId : 해당 계정의 접근 권한이 없습니다")))
                .build();
        return ResponseEntity.status(ex.getStatusCode().value()).body(dto);
    }
}
