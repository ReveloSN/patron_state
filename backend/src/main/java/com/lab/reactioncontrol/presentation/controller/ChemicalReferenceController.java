package com.lab.reactioncontrol.presentation.controller;

import com.lab.reactioncontrol.application.usecase.catalog.FindCompoundDetailsUseCase;
import com.lab.reactioncontrol.application.usecase.catalog.ListReactionRecipesUseCase;
import com.lab.reactioncontrol.application.usecase.catalog.SearchCompoundsUseCase;
import com.lab.reactioncontrol.presentation.dto.response.catalog.CompoundDetailsResponse;
import com.lab.reactioncontrol.presentation.dto.response.catalog.CompoundSummaryResponse;
import com.lab.reactioncontrol.presentation.dto.response.catalog.ReactionRecipeResponse;
import com.lab.reactioncontrol.presentation.mapper.catalog.CatalogResponseMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
public class ChemicalReferenceController {

    private final SearchCompoundsUseCase searchCompoundsUseCase;
    private final FindCompoundDetailsUseCase findCompoundDetailsUseCase;
    private final ListReactionRecipesUseCase listReactionRecipesUseCase;
    private final CatalogResponseMapper catalogResponseMapper;

    public ChemicalReferenceController(
            SearchCompoundsUseCase searchCompoundsUseCase,
            FindCompoundDetailsUseCase findCompoundDetailsUseCase,
            ListReactionRecipesUseCase listReactionRecipesUseCase,
            CatalogResponseMapper catalogResponseMapper
    ) {
        this.searchCompoundsUseCase = searchCompoundsUseCase;
        this.findCompoundDetailsUseCase = findCompoundDetailsUseCase;
        this.listReactionRecipesUseCase = listReactionRecipesUseCase;
        this.catalogResponseMapper = catalogResponseMapper;
    }

    @GetMapping("/compounds")
    public List<CompoundSummaryResponse> searchCompounds(
            @RequestParam(required = false, defaultValue = "") String query
    ) {
        return searchCompoundsUseCase.execute(query).stream()
                .map(catalogResponseMapper::toSummaryResponse)
                .toList();
    }

    @GetMapping("/compounds/{externalId}")
    public CompoundDetailsResponse getCompoundDetails(@PathVariable String externalId) {
        return findCompoundDetailsUseCase.execute(externalId)
                .map(catalogResponseMapper::toDetailsResponse)
                .orElseThrow(() -> new IllegalArgumentException("Compound '" + externalId + "' was not found."));
    }

    @GetMapping("/recipes")
    public List<ReactionRecipeResponse> listReactionRecipes() {
        return listReactionRecipesUseCase.execute().stream()
                .map(catalogResponseMapper::toRecipeResponse)
                .toList();
    }
}
