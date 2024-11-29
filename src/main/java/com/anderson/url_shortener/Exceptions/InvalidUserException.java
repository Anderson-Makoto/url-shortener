package com.anderson.url_shortener.Exceptions;

public class InvalidUserException extends Exception {
    public InvalidUserException() {
        super("Usuário inválido");
    }
}