package com.lab.reactioncontrol.application.usecase.catalog;

import com.lab.reactioncontrol.domain.catalog.ReactionRecipeCatalog;
import com.lab.reactioncontrol.domain.catalog.model.ReactionRecipe;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListReactionRecipesUseCase {

    private final ReactionRecipeCatalog reactionRecipeCatalog;

    public ListReactionRecipesUseCase(ReactionRecipeCatalog reactionRecipeCatalog) {
        this.reactionRecipeCatalog = reactionRecipeCatalog;
    }

    public List<ReactionRecipe> execute() {
        return reactionRecipeCatalog.findAll();
    }
}
