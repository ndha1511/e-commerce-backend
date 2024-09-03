package com.code.ecommercebackend.exceptions;

import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });
        return new ResponseError(HttpStatus.BAD_REQUEST.value(), errors);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(FileNotSupportedException.class)
    public Response handleFileTypeException(
            FileNotSupportedException ex) {
        List<String> errors = List.of(ex.getMessage());
        return new ResponseError(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), errors);
    }
}
