package com.kpi.project.model.exception;

public class ValidatorException extends IllegalArgumentException {

    public ValidatorException(String errorMessage) {
        super(errorMessage);
    }

    public ValidatorException(String message, Throwable cause) {
        super(message, cause);
    }
}
