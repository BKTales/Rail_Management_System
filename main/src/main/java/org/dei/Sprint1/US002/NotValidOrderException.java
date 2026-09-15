package org.dei.Sprint1.US002;

public class NotValidOrderException extends RuntimeException {
    public NotValidOrderException(String message) {
        super(message);
    }
}
