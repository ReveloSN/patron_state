package com.lab.reactioncontrol.application.usecase;

import com.lab.reactioncontrol.application.port.out.ChemicalReactionRepository;
import com.lab.reactioncontrol.domain.exception.ReactionNotFoundException;
import com.lab.reactioncontrol.domain.model.ChemicalReaction;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CancelReactionUseCase {

    private final ChemicalReactionRepository reactionRepository;

    public CancelReactionUseCase(ChemicalReactionRepository reactionRepository) {
        this.reactionRepository = reactionRepository;
    }

    public ChemicalReaction execute(UUID reactionId) {
        ChemicalReaction reaction = reactionRepository.findById(reactionId)
                .orElseThrow(() -> new ReactionNotFoundException(reactionId));

        reaction.cancel();
        return reactionRepository.save(reaction);
    }
}
