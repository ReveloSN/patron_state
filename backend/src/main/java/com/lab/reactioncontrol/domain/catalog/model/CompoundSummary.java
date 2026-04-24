package com.lab.reactioncontrol.domain.catalog.model;

public record CompoundSummary(
        String externalId,
        String name,
        String molecularFormula,
        double molecularWeight,
        CompoundRole primaryRole,
        CompoundSource source
) {
}
