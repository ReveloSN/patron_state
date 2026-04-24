package com.lab.reactioncontrol.presentation.dto.response.catalog;

import java.util.List;

public record ReactionRecipeResponse(
        String name,
        String summary,
        List<String> reactants,
        List<String> products,
        String catalyst
) {
}
