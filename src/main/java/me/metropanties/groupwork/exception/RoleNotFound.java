package me.metropanties.groupwork.exception;

public class RoleNotFound extends RuntimeException {

    public RoleNotFound(String message) {
        super(message);
    }

    public RoleNotFound(String message, Throwable throwable) {
        super(message, throwable);
    }

}
