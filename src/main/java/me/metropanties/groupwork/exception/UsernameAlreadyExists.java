package me.metropanties.groupwork.exception;

public class UsernameAlreadyExists extends RuntimeException {

    public UsernameAlreadyExists(String message) {
        super(message);
    }

    public UsernameAlreadyExists(String message, Throwable throwable) {
        super(message, throwable);
    }

}
