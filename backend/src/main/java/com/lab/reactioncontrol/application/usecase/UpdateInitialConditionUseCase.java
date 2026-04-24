package com.lab.reactioncontrol.application.usecase;

import com.lab.reactioncontrol.application.port.out.ChemicalReactionRepository;
import com.lab.reactioncontrol.domain.exception.ReactionNotFoundException;
import com.lab.reactioncontrol.domain.model.ChemicalReaction;
import com.lab.reactioncontrol.domain.model.ReactionCondition;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateInitialConditionUseCase {

    private final ChemicalReactionRepository reactionRepository;

    public UpdateInitialConditionUseCase(ChemicalReactionRepository reactionRepository) {
        this.reactionRepository = reactionRepository;
    }

    public ChemicalReaction execute(UUID reactionId, ReactionCondition condition, String catalyst) {
        ChemicalReaction reaction = reactionRepository.findById(reactionId)
                .orElseThrow(() -> new ReactionNotFoundException(reactionId));

        reaction.updateInitialCondition(condition, catalyst);
        return reactionRepository.save(reaction);
    }
}
