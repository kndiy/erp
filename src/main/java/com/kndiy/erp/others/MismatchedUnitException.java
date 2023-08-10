package com.kndiy.erp.others;

public class MismatchedUnitException extends Exception {
    public MismatchedUnitException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
    public MismatchedUnitException(String errorMessage) {
        super(errorMessage);
    }
}
