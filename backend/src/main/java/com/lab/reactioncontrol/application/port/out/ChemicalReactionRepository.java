package com.lab.reactioncontrol.application.port.out;

import com.lab.reactioncontrol.domain.model.ChemicalReaction;

import java.util.Optional;
import java.util.UUID;

public interface ChemicalReactionRepository {

    ChemicalReaction save(ChemicalReaction reaction);

    Optional<ChemicalReaction> findById(UUID reactionId);
}
