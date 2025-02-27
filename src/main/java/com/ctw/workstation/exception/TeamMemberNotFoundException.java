package com.ctw.workstation.exception;

import java.text.MessageFormat;

public class TeamMemberNotFoundException extends RuntimeException {
    public TeamMemberNotFoundException(Long id) {
        super(
                MessageFormat.format("TeamMember with id {0} not found.", id)
        );
    }
}
