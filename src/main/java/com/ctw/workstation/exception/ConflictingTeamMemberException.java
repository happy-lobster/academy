package com.ctw.workstation.exception;

import java.text.MessageFormat;

public class ConflictingTeamMemberException extends RuntimeException {
    public ConflictingTeamMemberException(String ctwId) {
        super(
                MessageFormat.format("TeamMember with ctwId {0} already exists.", ctwId)
        );
    }
}
