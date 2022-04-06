package com.smitimaheshwari.exceptions;

public class InvalidCommandException extends RuntimeException {

    public InvalidCommandException(final String exceptionMessage) {
        super(exceptionMessage);
    }
}
