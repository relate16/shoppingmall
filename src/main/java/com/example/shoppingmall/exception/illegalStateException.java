package com.example.shoppingmall.exception;

public class illegalStateException extends RuntimeException{
    public illegalStateException() {
        super();
    }

    public illegalStateException(String message) {
        super(message);
    }

    public illegalStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public illegalStateException(Throwable cause) {
        super(cause);
    }

    protected illegalStateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
