package com.commerce.handler;

import com.commerce.common.response.CustomErrorResponse;
import com.commerce.exception.UniqueConstraintViolationException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    void handleNoResultException(NoResultException e) {
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    CustomErrorResponse handleNoResultException(UniqueConstraintViolationException e) {
        String field = e.getField();
        return CustomErrorResponse.builder()
                .field(field)
                .message( field + " duplicated")
                .code("1")
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<CustomErrorResponse> handleBindException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        String defaultMessage = fieldError.getDefaultMessage();
        String field = fieldError.getField();


        CustomErrorResponse errorResponse =  CustomErrorResponse.builder()
                .message("`" + field + "` " + defaultMessage)
                .field(field)
                .code("2")
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }
}
