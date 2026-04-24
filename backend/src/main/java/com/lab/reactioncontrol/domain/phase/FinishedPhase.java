package com.lab.reactioncontrol.domain.phase;

public class FinishedPhase extends ReactionPhase {

    @Override
    public String getStatus() {
        return "Completed";
    }

    @Override
    public String getMessage() {
        return "Reaction completed successfully. The final summary is available for consultation.";
    }
}
