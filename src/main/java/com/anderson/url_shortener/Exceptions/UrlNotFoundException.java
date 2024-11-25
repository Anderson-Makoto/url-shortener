package com.anderson.url_shortener.Exceptions;

public class UrlNotFoundException extends Exception {
    public UrlNotFoundException() {
        super("Url não cadastrada");
    }
}
