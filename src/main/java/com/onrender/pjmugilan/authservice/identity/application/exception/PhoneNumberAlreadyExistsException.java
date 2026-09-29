package com.onrender.pjmugilan.authservice.identity.application.exception;

public class PhoneNumberAlreadyExistsException extends RuntimeException {

    public PhoneNumberAlreadyExistsException() {
        super("Phone number is already registered");
    }
}