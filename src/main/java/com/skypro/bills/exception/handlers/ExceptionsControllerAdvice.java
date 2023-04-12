package com.skypro.bills.exception.handlers;

import com.skypro.bills.exception.BadMeterParamException;
import com.skypro.bills.exception.MeterNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsControllerAdvice {

    @ExceptionHandler(MeterNotFoundException.class)
    public ResponseEntity<?> notFound() {
        return ResponseEntity.status(404).build();
    }

    @ExceptionHandler(BadMeterParamException.class)
    public ResponseEntity<?> badParam() {
        return ResponseEntity.status(400).build();
    }
}