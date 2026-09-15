package org.dei.Utils.Exceptions;

public class WarehousesFullException extends RuntimeException {
    public WarehousesFullException(String message) {
        super(message);
    }
}
