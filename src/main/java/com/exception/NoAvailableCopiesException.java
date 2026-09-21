package com.exception;

public class NoAvailableCopiesException extends RuntimeException {

    public NoAvailableCopiesException(int id) {
        super(String.format("There's no copies left for book with id %s", id));
    }
}
