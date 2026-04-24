package com.lab.reactioncontrol.presentation.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record RecordMeasurementRequest(
        @NotNull(message = "Temperature is required.")
        Double temperatureCelsius,
        @NotNull(message = "Pressure is required.")
        @DecimalMin(value = "0.0", message = "Pressure must be zero or greater.")
        Double pressureAtm,
        @NotNull(message = "Product concentration is required.")
        @DecimalMin(value = "0.0", message = "Product concentration must be at least 0.")
        @DecimalMax(value = "100.0", message = "Product concentration must be at most 100.")
        Double productConcentrationPercentage
) {
}
