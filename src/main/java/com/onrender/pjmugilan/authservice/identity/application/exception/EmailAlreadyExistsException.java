package com.onrender.pjmugilan.authservice.identity.application.exception;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException() {
        super("Email address is already registered");
    }
}