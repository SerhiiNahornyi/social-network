package com.kpi.project.model.exception;

public class ValidatorException extends IllegalArgumentException {
    public ValidatorException(String errorMessage) {
        super(errorMessage);
    }
}
