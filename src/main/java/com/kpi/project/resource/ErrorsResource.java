package com.kpi.project.resource;

import com.kpi.project.model.ErrorResponse;
import com.kpi.project.model.enums.ErrorTypes;
import com.kpi.project.model.exception.ValidatorException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorsResource extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ValidatorException.class})
    protected ResponseEntity<Object> handleValidationExceptions(ValidatorException ex, WebRequest request) {

        final ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorType(ErrorTypes.validation_error);
        errorResponse.setMessage(ex.getMessage());

        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleUncaughtException(Exception ex, WebRequest request) {

        final ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorType(ErrorTypes.server_error);
        errorResponse.setMessage(ex.getMessage());

        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
