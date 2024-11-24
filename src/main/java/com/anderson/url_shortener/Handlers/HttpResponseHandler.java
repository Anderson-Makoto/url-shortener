package com.anderson.url_shortener.Handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.anderson.url_shortener.Exceptions.InvalidUrlException;

public class HttpResponseHandler {
    public static ResponseEntity<?> handleHttpResponse(Exception e) {
        if (e instanceof InvalidUrlException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro de servidor");
    }
}