package com.lab.reactioncontrol.presentation.dto.response.catalog;

import java.util.List;

public record CompoundDetailsResponse(
        String externalId,
        String name,
        String molecularFormula,
        double molecularWeight,
        String source,
        String primaryRole,
        List<String> synonyms,
        List<CompoundIdentifierResponse> identifiers
) {
}
