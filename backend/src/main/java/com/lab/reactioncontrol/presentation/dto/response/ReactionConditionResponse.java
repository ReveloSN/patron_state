package com.lab.reactioncontrol.presentation.dto.response;

public record ReactionConditionResponse(
        double temperatureCelsius,
        double pressureAtm,
        double productConcentrationPercentage
) {
}
