package com.lab.reactioncontrol.domain.phase;

import com.lab.reactioncontrol.domain.model.ChemicalReaction;

public class SuspendedPhase extends ReactionPhase {

    @Override
    public String getStatus() {
        return "Paused";
    }

    @Override
    public String getMessage() {
        return "Reaction paused. Resume or cancel the execution.";
    }

    @Override
    public void resume(ChemicalReaction reaction) {
        reaction.changePhase(new ActivePhase());
    }

    @Override
    public void cancel(ChemicalReaction reaction) {
        reaction.changePhase(new AbortedPhase());
    }
}
