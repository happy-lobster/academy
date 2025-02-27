package com.ctw.workstation.exception;

import java.text.MessageFormat;

public class ConflictingTeamException extends RuntimeException {
    public ConflictingTeamException(String name) {
      super(
              MessageFormat.format("Team with name «{0}» already exists.", name)
      );
    }
}
