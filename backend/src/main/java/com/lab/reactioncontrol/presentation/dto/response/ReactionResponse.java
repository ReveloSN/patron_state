package com.lab.reactioncontrol.presentation.dto.response;

import java.util.List;
import java.util.UUID;

public record ReactionResponse(
        UUID id,
        String name,
        String status,
        String message,
        String catalyst,
        ReactionConditionResponse currentCondition,
        List<ReactantResponse> reactants,
        List<ReactionMeasurementResponse> measurements
) {
}
