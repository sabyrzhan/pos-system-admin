package kz.sabyrzhan.exceptions;

public class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException() {
        super("Too many requests.");
    }
}
