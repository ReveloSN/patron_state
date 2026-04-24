package com.lab.reactioncontrol.presentation.dto.response.catalog;

public record CompoundSummaryResponse(
        String externalId,
        String name,
        String molecularFormula,
        double molecularWeight,
        String source,
        String primaryRole
) {
}
