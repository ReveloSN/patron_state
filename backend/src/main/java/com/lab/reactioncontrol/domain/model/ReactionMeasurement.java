package com.lab.reactioncontrol.domain.model;

import com.lab.reactioncontrol.domain.exception.ReactionValidationException;

import java.time.Instant;

public record ReactionMeasurement(
        double temperatureCelsius,
        double pressureAtm,
        double productConcentrationPercentage,
        Instant recordedAt
) {

    public ReactionMeasurement {
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
        if (recordedAt == null) {
            throw new ReactionValidationException("Measurement timestamp is required.");
        }
    }

    public static ReactionMeasurement now(
            double temperatureCelsius,
            double pressureAtm,
            double productConcentrationPercentage
    ) {
        return new ReactionMeasurement(
                temperatureCelsius,
                pressureAtm,
                productConcentrationPercentage,
                Instant.now()
        );
    }

    public ReactionCondition toCondition() {
        return new ReactionCondition(temperatureCelsius, pressureAtm, productConcentrationPercentage);
    }
}
