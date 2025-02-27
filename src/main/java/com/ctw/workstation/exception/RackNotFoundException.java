package com.ctw.workstation.exception;

import java.text.MessageFormat;

public class RackNotFoundException extends RuntimeException {
    public RackNotFoundException(Long id) {
        super(
                MessageFormat.format("Rack with id {0} not found.", id)
        );
    }
}
