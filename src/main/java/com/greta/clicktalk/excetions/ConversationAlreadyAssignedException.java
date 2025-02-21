package com.greta.clicktalk.excetions;


public class ConversationAlreadyAssignedException extends RuntimeException {
    public ConversationAlreadyAssignedException(String message) {
        super(message);
    }
}
