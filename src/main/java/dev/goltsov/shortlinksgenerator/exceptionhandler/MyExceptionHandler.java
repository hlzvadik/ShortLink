package dev.goltsov.shortlinksgenerator.exceptionhandler;

import dev.goltsov.shortlinksgenerator.exception.LinkExpiredException;
import dev.goltsov.shortlinksgenerator.exception.LinkNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler(LinkExpiredException.class)
    public ResponseEntity<Map<String, String>> linkExpired(LinkExpiredException e) {
        return ResponseEntity.status(HttpStatus.GONE)
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(LinkNotFoundException.class)
    public ResponseEntity<Map<String, String>> linkNotFound(LinkNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> other(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "internal error"));
    }
}
