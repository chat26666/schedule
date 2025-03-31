package com.example.schedule.exception;
import com.example.schedule.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponseDto> buildErrorResponse(
            HttpServletRequest request,
            HttpStatusCode statusCode,
            String message,
            List<String> errorList) {
        ErrorResponseDto dto = ErrorResponseDto.builder()
                .timestamp(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date()))
                .status(statusCode.value())
                .error(message)
                .path(request.getRequestURI())
                .fieldErrors(errorList)
                .build();
        return ResponseEntity.status(statusCode).body(dto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handlerMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errorList = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " : " + error.getDefaultMessage()).toList();
        return buildErrorResponse(request, HttpStatus.BAD_REQUEST, "Validation Failed", errorList);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handlerConstraintViolationException(
            ConstraintViolationException ex, HttpServletRequest request) {
        List<String> errorList = ex.getConstraintViolations().stream()
                .map(violation -> {
                    String fullPath = violation.getPropertyPath().toString();
                    String[] path = fullPath.split("\\.");
                    String lastPath = path[path.length - 1];
                    return lastPath + " : " + violation.getMessage();
                })
                .toList();
        return buildErrorResponse(request, HttpStatus.BAD_REQUEST, "Validation Failed", errorList);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDto> handlerResponseStatusException(
            ResponseStatusException ex, HttpServletRequest request) {
        HttpStatusCode statusCode = ex.getStatusCode();
        String message;
        switch (statusCode.value()) {
            case 401:
                message = "Authentication Failed";
                break;
            case 403:
                message = "Access Denied";
                break;
            default:
                message = "Not Found";
                break;
        }
        return buildErrorResponse(request, ex.getStatusCode(), message,
                new ArrayList<>(List.of(ex.getReason())));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String error = ex.getName() + " : " + ex.getRequiredType().getSimpleName() + " 타입의 값을 입력해주십시오";
        List<String> errorList = new ArrayList<>(List.of(error));
        return buildErrorResponse(request, HttpStatus.BAD_REQUEST, "Type Mismatch", errorList);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handlerSQLIntegrityConstraintViolationException(
            SQLIntegrityConstraintViolationException ex, HttpServletRequest request) {
        String error = "email : 이미 중복된 이메일이 존재합니다";
        List<String> errorList = new ArrayList<>(List.of(error));
        return buildErrorResponse(request, HttpStatus.CONFLICT, "Data Integrity Constraint Violation", errorList);
    }
}
