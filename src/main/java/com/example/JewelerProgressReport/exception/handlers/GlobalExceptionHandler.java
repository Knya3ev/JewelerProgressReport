package com.example.JewelerProgressReport.exception.handlers;

import com.example.JewelerProgressReport.exception.HttpException;
import com.example.JewelerProgressReport.exception.error.FieldsValidationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> catchHttpException(HttpException e) {
        return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FieldsValidationError> catchModelFieldDataValidationException(
            MethodArgumentNotValidException e){
        log.error(String.valueOf(e));
        Map<String, String> errors = new HashMap<>();
        BindingResult bindingResult = e.getBindingResult();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return new ResponseEntity<>(new FieldsValidationError(errors), HttpStatus.BAD_REQUEST);
    }
}
