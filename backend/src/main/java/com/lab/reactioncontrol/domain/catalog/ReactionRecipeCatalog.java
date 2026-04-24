package com.lab.reactioncontrol.domain.catalog;

import com.lab.reactioncontrol.domain.catalog.model.ReactionRecipe;

import java.util.List;

public interface ReactionRecipeCatalog {

    List<ReactionRecipe> findAll();
}
