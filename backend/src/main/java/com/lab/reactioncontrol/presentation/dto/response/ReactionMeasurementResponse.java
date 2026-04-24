package com.lab.reactioncontrol.presentation.dto.response;

import java.time.Instant;

public record ReactionMeasurementResponse(
        double temperatureCelsius,
        double pressureAtm,
        double productConcentrationPercentage,
        Instant recordedAt
) {
}
