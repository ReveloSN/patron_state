package com.lab.reactioncontrol.domain.policy;

import com.lab.reactioncontrol.domain.model.ReactionCondition;

public class ReactionSafetyPolicy {

    public static final double MAX_TEMPERATURE_CELSIUS = 120.0;
    public static final double MAX_PRESSURE_ATM = 5.0;
    public static final double MAX_CONCENTRATION_VARIATION = 30.0;

    public boolean isUnsafe(ReactionCondition previousCondition, ReactionCondition currentCondition) {
        return exceedsTemperatureLimit(currentCondition)
                || exceedsPressureLimit(currentCondition)
                || exceedsConcentrationVariation(previousCondition, currentCondition);
    }

    public boolean canStabilize(ReactionCondition currentCondition, ReactionCondition candidateCondition) {
        return !exceedsTemperatureLimit(candidateCondition)
                && !exceedsPressureLimit(candidateCondition)
                && !exceedsConcentrationVariation(currentCondition, candidateCondition);
    }

    private boolean exceedsTemperatureLimit(ReactionCondition condition) {
        return condition.temperatureCelsius() > MAX_TEMPERATURE_CELSIUS;
    }

    private boolean exceedsPressureLimit(ReactionCondition condition) {
        return condition.pressureAtm() > MAX_PRESSURE_ATM;
    }

    private boolean exceedsConcentrationVariation(
            ReactionCondition previousCondition,
            ReactionCondition currentCondition
    ) {
        return previousCondition.concentrationDelta(currentCondition) > MAX_CONCENTRATION_VARIATION;
    }
}
