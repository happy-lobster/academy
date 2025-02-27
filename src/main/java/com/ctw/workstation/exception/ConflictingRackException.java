package com.ctw.workstation.exception;

import java.text.MessageFormat;

public class ConflictingRackException extends RuntimeException {
    public ConflictingRackException(String serialNumber) {
      super(
              MessageFormat.format("Rack with serial number {0} already exists.", serialNumber)
      );
    }
}
