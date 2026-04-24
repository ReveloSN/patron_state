package com.lab.reactioncontrol.domain.phase;

import com.lab.reactioncontrol.domain.exception.ReactionValidationException;
import com.lab.reactioncontrol.domain.model.ChemicalReaction;
import com.lab.reactioncontrol.domain.model.Reactant;
import com.lab.reactioncontrol.domain.model.ReactionCondition;

public class PreparedPhase extends ReactionPhase {

    @Override
    public String getStatus() {
        return "Prepared";
    }

    @Override
    public String getMessage() {
        return "Reaction prepared. Add reactants, adjust initial conditions, or start the execution.";
    }

    @Override
    public void addReactant(ChemicalReaction reaction, Reactant reactant) {
        reaction.appendReactant(reactant);
    }

    @Override
    public void updateInitialCondition(
            ChemicalReaction reaction,
            ReactionCondition newCondition,
            String catalyst
    ) {
        reaction.updateCurrentCondition(newCondition);
        reaction.updateCatalyst(catalyst);
    }

    @Override
    public void start(ChemicalReaction reaction) {
        if (!reaction.hasReactants()) {
            throw new ReactionValidationException("The reaction must contain at least one reactant before it can start.");
        }
        reaction.changePhase(new ActivePhase());
    }
}
