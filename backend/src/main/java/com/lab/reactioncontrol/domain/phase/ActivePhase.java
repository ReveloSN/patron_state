package com.lab.reactioncontrol.domain.phase;

import com.lab.reactioncontrol.domain.model.ChemicalReaction;
import com.lab.reactioncontrol.domain.model.ReactionCondition;
import com.lab.reactioncontrol.domain.model.ReactionMeasurement;
import com.lab.reactioncontrol.domain.policy.ReactionSafetyPolicy;

public class ActivePhase extends ReactionPhase {

    @Override
    public String getStatus() {
        return "InProgress";
    }

    @Override
    public String getMessage() {
        return "Reaction in progress. Measurements can be recorded while conditions remain safe.";
    }

    @Override
    public void recordMeasurement(
            ChemicalReaction reaction,
            ReactionMeasurement measurement,
            ReactionSafetyPolicy safetyPolicy
    ) {
        ReactionCondition previousCondition = reaction.getCurrentCondition();
        ReactionCondition measuredCondition = measurement.toCondition();

        reaction.appendMeasurement(measurement);
        reaction.updateCurrentCondition(measuredCondition);

        if (safetyPolicy.isUnsafe(previousCondition, measuredCondition)) {
            reaction.changePhase(new HazardousPhase());
        }
    }

    @Override
    public void pause(ChemicalReaction reaction) {
        reaction.changePhase(new SuspendedPhase());
    }

    @Override
    public void complete(ChemicalReaction reaction) {
        reaction.changePhase(new FinishedPhase());
    }

    @Override
    public void cancel(ChemicalReaction reaction) {
        reaction.changePhase(new AbortedPhase());
    }
}
