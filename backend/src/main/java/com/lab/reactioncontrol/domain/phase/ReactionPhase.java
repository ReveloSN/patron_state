package com.lab.reactioncontrol.domain.phase;

import com.lab.reactioncontrol.domain.exception.InvalidReactionOperationException;
import com.lab.reactioncontrol.domain.model.ChemicalReaction;
import com.lab.reactioncontrol.domain.model.Reactant;
import com.lab.reactioncontrol.domain.model.ReactionCondition;
import com.lab.reactioncontrol.domain.model.ReactionMeasurement;
import com.lab.reactioncontrol.domain.policy.ReactionSafetyPolicy;

public abstract class ReactionPhase {

    public abstract String getStatus();

    public abstract String getMessage();

    public void addReactant(ChemicalReaction reaction, Reactant reactant) {
        throw invalidOperation("add reactants", reaction);
    }

    public void updateInitialCondition(
            ChemicalReaction reaction,
            ReactionCondition newCondition,
            String catalyst
    ) {
        throw invalidOperation("update initial conditions", reaction);
    }

    public void start(ChemicalReaction reaction) {
        throw invalidOperation("start the reaction", reaction);
    }

    public void recordMeasurement(
            ChemicalReaction reaction,
            ReactionMeasurement measurement,
            ReactionSafetyPolicy safetyPolicy
    ) {
        throw invalidOperation("record measurements", reaction);
    }

    public void pause(ChemicalReaction reaction) {
        throw invalidOperation("pause the reaction", reaction);
    }

    public void resume(ChemicalReaction reaction) {
        throw invalidOperation("resume the reaction", reaction);
    }

    public void stabilize(
            ChemicalReaction reaction,
            ReactionMeasurement safeMeasurement,
            ReactionSafetyPolicy safetyPolicy
    ) {
        throw invalidOperation("stabilize the reaction", reaction);
    }

    public void complete(ChemicalReaction reaction) {
        throw invalidOperation("complete the reaction", reaction);
    }

    public void cancel(ChemicalReaction reaction) {
        throw invalidOperation("cancel the reaction", reaction);
    }

    protected InvalidReactionOperationException invalidOperation(String action, ChemicalReaction reaction) {
        return new InvalidReactionOperationException(
                "Cannot " + action + " while the reaction is " + reaction.getStatus() + "."
        );
    }
}
