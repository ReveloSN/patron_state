package com.lab.reactioncontrol.domain.model;

import com.lab.reactioncontrol.domain.exception.ReactionValidationException;

public record ReactionCondition(
        double temperatureCelsius,
        double pressureAtm,
        double productConcentrationPercentage
) {

    public ReactionCondition {
        if (Double.isNaN(temperatureCelsius) || Double.isInfinite(temperatureCelsius)) {
            throw new ReactionValidationException("Temperature must be a finite value.");
        }
        if (Double.isNaN(pressureAtm) || Double.isInfinite(pressureAtm) || pressureAtm < 0) {
            throw new ReactionValidationException("Pressure must be zero or greater.");
        }
        if (Double.isNaN(productConcentrationPercentage)
                || Double.isInfinite(productConcentrationPercentage)
                || productConcentrationPercentage < 0
                || productConcentrationPercentage > 100) {
            throw new ReactionValidationException("Product concentration must be between 0 and 100.");
        }
    }

    public double concentrationDelta(ReactionCondition otherCondition) {
        return Math.abs(productConcentrationPercentage - otherCondition.productConcentrationPercentage);
    }
}
