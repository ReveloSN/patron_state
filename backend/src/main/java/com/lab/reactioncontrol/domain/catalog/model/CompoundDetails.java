package com.lab.reactioncontrol.domain.catalog.model;

import java.util.List;

public record CompoundDetails(
        String externalId,
        String name,
        String molecularFormula,
        double molecularWeight,
        List<String> synonyms,
        CompoundRole primaryRole,
        CompoundSource source,
        List<CompoundIdentifier> identifiers
) {

    public CompoundSummary toSummary() {
        return new CompoundSummary(
                externalId,
                name,
                molecularFormula,
                molecularWeight,
                primaryRole,
                source
        );
    }
}
