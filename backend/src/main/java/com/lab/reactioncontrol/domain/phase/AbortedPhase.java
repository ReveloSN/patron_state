package com.lab.reactioncontrol.domain.phase;

public class AbortedPhase extends ReactionPhase {

    @Override
    public String getStatus() {
        return "Cancelled";
    }

    @Override
    public String getMessage() {
        return "Reaction cancelled. Recorded information remains available for consultation.";
    }
}
