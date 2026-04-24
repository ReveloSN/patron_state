package com.lab.reactioncontrol.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddReactantRequest(
        @NotBlank(message = "Reactant name is required.")
        String name,
        @NotNull(message = "Reactant quantity is required.")
        @Positive(message = "Reactant quantity must be greater than zero.")
        Double quantity,
        @NotBlank(message = "Reactant unit is required.")
        String unit
) {
}
