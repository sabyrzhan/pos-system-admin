package kz.sabyrzhan.exceptions;

public class InvalidOrderItemsException extends RuntimeException {
    public InvalidOrderItemsException(String message) {
        super(message);
    }
}
