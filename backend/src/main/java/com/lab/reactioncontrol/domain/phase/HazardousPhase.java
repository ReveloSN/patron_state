package com.lab.reactioncontrol.domain.phase;

import com.lab.reactioncontrol.domain.exception.ReactionValidationException;
import com.lab.reactioncontrol.domain.model.ChemicalReaction;
import com.lab.reactioncontrol.domain.model.ReactionCondition;
import com.lab.reactioncontrol.domain.model.ReactionMeasurement;
import com.lab.reactioncontrol.domain.policy.ReactionSafetyPolicy;

public class HazardousPhase extends ReactionPhase {

    @Override
    public String getStatus() {
        return "Unstable";
    }

    @Override
    public String getMessage() {
        return "Warning: unsafe reaction conditions detected. Stabilize or cancel the reaction.";
    }

    @Override
    public void stabilize(
            ChemicalReaction reaction,
            ReactionMeasurement safeMeasurement,
            ReactionSafetyPolicy safetyPolicy
    ) {
        ReactionCondition currentCondition = reaction.getCurrentCondition();
        ReactionCondition safeCondition = safeMeasurement.toCondition();

        if (!safetyPolicy.canStabilize(currentCondition, safeCondition)) {
            throw new ReactionValidationException(
                    "Safe stabilization conditions are required to return the reaction to execution."
            );
        }

        reaction.appendMeasurement(safeMeasurement);
        reaction.updateCurrentCondition(safeCondition);
        reaction.changePhase(new ActivePhase());
    }

    @Override
    public void cancel(ChemicalReaction reaction) {
        reaction.changePhase(new AbortedPhase());
    }
}
