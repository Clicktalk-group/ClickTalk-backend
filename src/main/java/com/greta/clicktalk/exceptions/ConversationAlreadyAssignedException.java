package com.greta.clicktalk.exceptions;


public class ConversationAlreadyAssignedException extends RuntimeException {
    public ConversationAlreadyAssignedException(String message) {
        super(message);
    }
}
