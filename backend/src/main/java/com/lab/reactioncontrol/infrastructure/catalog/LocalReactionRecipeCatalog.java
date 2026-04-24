package com.lab.reactioncontrol.infrastructure.catalog;

import com.lab.reactioncontrol.domain.catalog.ReactionRecipeCatalog;
import com.lab.reactioncontrol.domain.catalog.model.ReactionRecipe;

import java.util.List;

public class LocalReactionRecipeCatalog implements ReactionRecipeCatalog {

    private final List<ReactionRecipe> recipes = List.of(
            new ReactionRecipe(
                    "Esterification",
                    "Produces ethyl acetate from acetic acid and ethanol using sulfuric acid as catalyst.",
                    List.of("Acetic Acid", "Ethanol"),
                    List.of("Ethyl Acetate", "Water"),
                    "Sulfuric Acid"
            ),
            new ReactionRecipe(
                    "Acid-Base Neutralization",
                    "Demonstrates salt and water formation from hydrochloric acid and sodium hydroxide.",
                    List.of("Hydrochloric Acid", "Sodium Hydroxide"),
                    List.of("Sodium Chloride", "Water"),
                    null
            ),
            new ReactionRecipe(
                    "Hydrogen Peroxide Decomposition",
                    "Shows catalytic decomposition of hydrogen peroxide into oxygen and water.",
                    List.of("Hydrogen Peroxide"),
                    List.of("Water", "Oxygen"),
                    "Manganese Dioxide"
            )
    );

    @Override
    public List<ReactionRecipe> findAll() {
        return recipes;
    }
}
