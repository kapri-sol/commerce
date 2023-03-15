package com.commerce.exception;

public class UniqueConstraintViolationException extends RuntimeException {
    public UniqueConstraintViolationException() {
        super("Constraint Unique Violation");
    }
}
