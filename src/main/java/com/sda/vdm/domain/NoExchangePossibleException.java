package com.sda.vdm.domain;

public class NoExchangePossibleException extends Exception {
    public NoExchangePossibleException() {
    }

    public NoExchangePossibleException(String message) {
        super(message);
    }

    public NoExchangePossibleException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoExchangePossibleException(Throwable cause) {
        super(cause);
    }

    public NoExchangePossibleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
