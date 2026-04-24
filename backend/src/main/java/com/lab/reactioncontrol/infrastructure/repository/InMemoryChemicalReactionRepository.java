package com.lab.reactioncontrol.infrastructure.repository;

import com.lab.reactioncontrol.application.port.out.ChemicalReactionRepository;
import com.lab.reactioncontrol.domain.model.ChemicalReaction;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryChemicalReactionRepository implements ChemicalReactionRepository {

    private final Map<UUID, ChemicalReaction> storage = new ConcurrentHashMap<>();

    @Override
    public ChemicalReaction save(ChemicalReaction reaction) {
        storage.put(reaction.getId(), reaction);
        return reaction;
    }

    @Override
    public Optional<ChemicalReaction> findById(UUID reactionId) {
        return Optional.ofNullable(storage.get(reactionId));
    }
}
