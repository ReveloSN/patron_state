package com.lab.reactioncontrol.presentation.mapper.catalog;

import com.lab.reactioncontrol.domain.catalog.model.CompoundDetails;
import com.lab.reactioncontrol.domain.catalog.model.CompoundIdentifier;
import com.lab.reactioncontrol.domain.catalog.model.CompoundSummary;
import com.lab.reactioncontrol.domain.catalog.model.ReactionRecipe;
import com.lab.reactioncontrol.presentation.dto.response.catalog.CompoundDetailsResponse;
import com.lab.reactioncontrol.presentation.dto.response.catalog.CompoundIdentifierResponse;
import com.lab.reactioncontrol.presentation.dto.response.catalog.CompoundSummaryResponse;
import com.lab.reactioncontrol.presentation.dto.response.catalog.ReactionRecipeResponse;
import org.springframework.stereotype.Component;

@Component
public class CatalogResponseMapper {

    public CompoundSummaryResponse toSummaryResponse(CompoundSummary compoundSummary) {
        return new CompoundSummaryResponse(
                compoundSummary.externalId(),
                compoundSummary.name(),
                compoundSummary.molecularFormula(),
                compoundSummary.molecularWeight(),
                compoundSummary.source().name(),
                compoundSummary.primaryRole() == null ? null : compoundSummary.primaryRole().name()
        );
    }

    public CompoundDetailsResponse toDetailsResponse(CompoundDetails compoundDetails) {
        return new CompoundDetailsResponse(
                compoundDetails.externalId(),
                compoundDetails.name(),
                compoundDetails.molecularFormula(),
                compoundDetails.molecularWeight(),
                compoundDetails.source().name(),
                compoundDetails.primaryRole() == null ? null : compoundDetails.primaryRole().name(),
                compoundDetails.synonyms(),
                compoundDetails.identifiers().stream().map(this::toIdentifierResponse).toList()
        );
    }

    public ReactionRecipeResponse toRecipeResponse(ReactionRecipe reactionRecipe) {
        return new ReactionRecipeResponse(
                reactionRecipe.name(),
                reactionRecipe.summary(),
                reactionRecipe.reactants(),
                reactionRecipe.products(),
                reactionRecipe.catalyst()
        );
    }

    private CompoundIdentifierResponse toIdentifierResponse(CompoundIdentifier compoundIdentifier) {
        return new CompoundIdentifierResponse(
                compoundIdentifier.label(),
                compoundIdentifier.value()
        );
    }
}
