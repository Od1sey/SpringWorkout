package com.exception;

public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException(String entity, Integer id) {
        super(String.format("%s with id %d doesn't exist", entity, id));
    }

}
