package me.metropanties.groupwork.exception;

public class S3ObjectAlreadyExists extends RuntimeException {

    public S3ObjectAlreadyExists(String message) {
        super(message);
    }

    public S3ObjectAlreadyExists(String message, Throwable throwable) {
        super(message, throwable);
    }

}
