package com.lab.reactioncontrol.domain.catalog.model;

import java.util.List;

public record ReactionRecipe(
        String name,
        String summary,
        List<String> reactants,
        List<String> products,
        String catalyst
) {
}
