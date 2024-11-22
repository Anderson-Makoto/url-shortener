package com.anderson.url_shortener.Exceptions;

public class InvalidUrlException extends Exception {
    public InvalidUrlException() {
        super("Url invalida");
    }
}
