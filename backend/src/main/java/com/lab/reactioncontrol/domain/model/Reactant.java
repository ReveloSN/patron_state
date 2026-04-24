package com.lab.reactioncontrol.domain.model;

import com.lab.reactioncontrol.domain.exception.ReactionValidationException;

public record Reactant(String name, double quantity, String unit) {

    public Reactant {
        if (name == null || name.isBlank()) {
            throw new ReactionValidationException("Reactant name is required.");
        }
        if (Double.isNaN(quantity) || Double.isInfinite(quantity) || quantity <= 0) {
            throw new ReactionValidationException("Reactant quantity must be greater than zero.");
        }
        if (unit == null || unit.isBlank()) {
            throw new ReactionValidationException("Reactant unit is required.");
        }
    }
}
