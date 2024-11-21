package com.code.ecommercebackend.exceptions;

import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseError;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
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

    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(FileNotSupportedException.class)
    public Response handleFileTypeException(
            FileNotSupportedException ex) {
        List<String> errors = List.of(ex.getMessage());
        return new ResponseError(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), errors);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseError handleAuthenticationException(AuthenticationException ex) {
        return new ResponseError(HttpStatus.UNAUTHORIZED.value(), List.of(ex.getMessage()));
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseError handleExpiredToken(ExpiredJwtException ex) {
        return new ResponseError(HttpStatus.UNAUTHORIZED.value(), List.of(ex.getMessage()));
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataNotMatchedException.class)
    public ResponseError handleDataNotMatchedException(DataNotMatchedException ex) {
        return new ResponseError(HttpStatus.BAD_REQUEST.value(), List.of(ex.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataExpiredException.class)
    public ResponseError handleDataExpiredException(DataExpiredException ex) {
        return new ResponseError(HttpStatus.BAD_REQUEST.value(), List.of(ex.getMessage()));
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UserNotVerifyException.class)
    public ResponseError handleNotVerifyEmail(UserNotVerifyException ex) {
        return new ResponseError(HttpStatus.FORBIDDEN.value(), List.of(ex.getMessage()));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseError handleDataNotFoundException(DataNotFoundException ex) {
        return new ResponseError(HttpStatus.NOT_FOUND.value(), List.of(ex.getMessage()));
    }




}
