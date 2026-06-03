package com.mprribeiro.app_ai_crafter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ActionNotAllowedException.class)
    public ProblemDetail handleActionNotAllowed(ActionNotAllowedException ex) {
        return getProblemDetail(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ProblemDetail handleProjectNotFound(ProjectNotFoundException ex) {
        return getProblemDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFound(UserNotFoundException ex) {
        return getProblemDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail badRequest(BadRequestException ex) {
        return getProblemDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail badRequest(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        List<Map<String, String>> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> {
                    return Map.of(
                            "field", error.getField(),
                            "message", error.getDefaultMessage()
                    );
                })
                .toList();

        problemDetail.setDetail(fieldErrors.toString());
        return problemDetail;
    }

    private ProblemDetail getProblemDetail(HttpStatus status, String message) {
        final var problemDetail = ProblemDetail.forStatusAndDetail(status, message);
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
