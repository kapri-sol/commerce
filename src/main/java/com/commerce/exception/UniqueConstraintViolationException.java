package com.commerce.exception;

import lombok.Getter;

@Getter
public class UniqueConstraintViolationException extends RuntimeException {
    private String field;

    public UniqueConstraintViolationException(String field) {
        super("Constraint Unique Violation");
        this.field = field;
    }
}
