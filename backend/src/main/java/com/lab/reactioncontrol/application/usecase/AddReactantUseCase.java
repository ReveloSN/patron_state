package com.lab.reactioncontrol.application.usecase;

import com.lab.reactioncontrol.application.port.out.ChemicalReactionRepository;
import com.lab.reactioncontrol.domain.exception.ReactionNotFoundException;
import com.lab.reactioncontrol.domain.model.ChemicalReaction;
import com.lab.reactioncontrol.domain.model.Reactant;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AddReactantUseCase {

    private final ChemicalReactionRepository reactionRepository;

    public AddReactantUseCase(ChemicalReactionRepository reactionRepository) {
        this.reactionRepository = reactionRepository;
    }

    public ChemicalReaction execute(UUID reactionId, Reactant reactant) {
        ChemicalReaction reaction = reactionRepository.findById(reactionId)
                .orElseThrow(() -> new ReactionNotFoundException(reactionId));

        reaction.addReactant(reactant);
        return reactionRepository.save(reaction);
    }
}
