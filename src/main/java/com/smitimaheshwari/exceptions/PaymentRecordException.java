package com.smitimaheshwari.exceptions;

public class PaymentRecordException extends RuntimeException {

    public PaymentRecordException(final String exceptionMessage) {
        super(exceptionMessage);
    }
}
