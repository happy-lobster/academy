package com.ctw.workstation.exception;

public class ConflictingBookingException extends RuntimeException {
    public ConflictingBookingException(String message) {
        super(message);
    }
}
