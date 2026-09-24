package com.banfico.banking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // This class should handle exceptions thrown by my REST controllers across the
                      // application
public class GlobalExceptionHandler {
        @ExceptionHandler(CustomerNotFoundException.class)
        public ResponseEntity<String> handleCustomerNotFound(
                        CustomerNotFoundException exception) {

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(exception.getMessage());
        }

        @ExceptionHandler(BankAccountNotFoundException.class)
        public ResponseEntity<String> handleBankAccountNotFound(
                        BankAccountNotFoundException exception) {

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(exception.getMessage());
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<String> handleIllegalArgumentException(
                        IllegalArgumentException exception) {

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(exception.getMessage());
        }

        @ExceptionHandler(TransactionNotFoundException.class)
        public ResponseEntity<String> handleTransactionNotFound(
                        TransactionNotFoundException exception) {

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(exception.getMessage());
        }
        @ExceptionHandler(ConsentNotFoundException.class)
public ResponseEntity<String> handleConsentNotFound(
        ConsentNotFoundException exception) {

    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(exception.getMessage());
}
}
