package server.exceptions;

public class IllegalFieldValueException extends RuntimeException {
    public IllegalFieldValueException(String message) {
        super(message);
    }
}
