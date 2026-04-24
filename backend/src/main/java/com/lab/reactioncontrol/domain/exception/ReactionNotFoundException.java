package com.lab.reactioncontrol.domain.exception;

import java.util.UUID;

public class ReactionNotFoundException extends RuntimeException {

    public ReactionNotFoundException(UUID reactionId) {
        super("Reaction with id '" + reactionId + "' was not found.");
    }
}
