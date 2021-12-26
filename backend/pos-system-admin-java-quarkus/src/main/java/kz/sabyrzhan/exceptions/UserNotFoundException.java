package kz.sabyrzhan.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Throwable t) {
        super("User not found", t);
    }
}
