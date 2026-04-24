package com.lab.reactioncontrol.domain.model;

import com.lab.reactioncontrol.domain.exception.InvalidReactionOperationException;
import com.lab.reactioncontrol.domain.policy.ReactionSafetyPolicy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ChemicalReactionStateTest {

    private final ReactionSafetyPolicy safetyPolicy = new ReactionSafetyPolicy();

    @Test
    void shouldStartReactionFromPreparedPhase() {
        ChemicalReaction reaction = ChemicalReaction.create(
                "Esterification",
                new ReactionCondition(25.0, 1.0, 0.0),
                "Sulfuric Acid"
        );

        reaction.addReactant(new Reactant("Acetic Acid", 25.0, "mL"));
        reaction.start();

        assertEquals("InProgress", reaction.getStatus());
    }

    @Test
    void shouldRejectAddingReactantsAfterReactionHasStarted() {
        ChemicalReaction reaction = ChemicalReaction.create(
                "Esterification",
                new ReactionCondition(25.0, 1.0, 0.0),
                null
        );

        reaction.addReactant(new Reactant("Acetic Acid", 25.0, "mL"));
        reaction.start();

        assertThrows(
                InvalidReactionOperationException.class,
                () -> reaction.addReactant(new Reactant("Ethanol", 30.0, "mL"))
        );
    }

    @Test
    void shouldMoveReactionToUnstableWhenMeasurementExceedsTemperatureLimit() {
        ChemicalReaction reaction = ChemicalReaction.create(
                "Esterification",
                new ReactionCondition(25.0, 1.0, 0.0),
                null
        );

        reaction.addReactant(new Reactant("Acetic Acid", 25.0, "mL"));
        reaction.start();
        reaction.recordMeasurement(ReactionMeasurement.now(130.0, 1.2, 20.0), safetyPolicy);

        assertEquals("Unstable", reaction.getStatus());
        assertEquals(1, reaction.getMeasurements().size());
    }

    @Test
    void shouldStabilizeReactionAndReturnToInProgress() {
        ChemicalReaction reaction = ChemicalReaction.create(
                "Esterification",
                new ReactionCondition(25.0, 1.0, 0.0),
                null
        );

        reaction.addReactant(new Reactant("Acetic Acid", 25.0, "mL"));
        reaction.start();
        reaction.recordMeasurement(ReactionMeasurement.now(130.0, 1.2, 20.0), safetyPolicy);

        reaction.stabilize(ReactionMeasurement.now(90.0, 1.0, 25.0), safetyPolicy);

        assertEquals("InProgress", reaction.getStatus());
        assertEquals(2, reaction.getMeasurements().size());
    }
}
