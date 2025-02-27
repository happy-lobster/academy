package com.ctw.workstation.exception;

import java.text.MessageFormat;

public class TeamNotFoundException extends RuntimeException {
    public TeamNotFoundException(Long id) {
        super(
                MessageFormat.format("Team with id {0} not found.", id)
        );
    }
}
