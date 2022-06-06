package me.metropanties.groupwork.exception;

public class S3ObjectNotFound extends RuntimeException {

    public S3ObjectNotFound(String message) {
        super(message);
    }

    public S3ObjectNotFound(String message, Throwable throwable) {
        super(message, throwable);
    }

}
