package org.author.demo.filemanager.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> badRequestHandler(IllegalArgumentException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(status, exception.getMessage());

        return ResponseEntity.status(status).body(problemDetail);
    }

    @ExceptionHandler({NoSuchElementException.class, FileNotFoundException.class})
    public ResponseEntity<ProblemDetail> notFoundHandler(Exception exception) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(status, exception.getMessage());

        return ResponseEntity.status(status).body(problemDetail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> internalServerErrorHandler(Exception exception) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(status, exception.getMessage());

        return ResponseEntity.status(status).body(problemDetail);
    }
}
