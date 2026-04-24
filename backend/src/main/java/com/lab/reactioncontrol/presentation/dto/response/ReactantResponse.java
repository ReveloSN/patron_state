package com.lab.reactioncontrol.presentation.dto.response;

public record ReactantResponse(
        String name,
        double quantity,
        String unit
) {
}
