package com.lab.reactioncontrol.application.usecase;

import com.lab.reactioncontrol.application.port.out.ChemicalReactionRepository;
import com.lab.reactioncontrol.domain.exception.ReactionNotFoundException;
import com.lab.reactioncontrol.domain.model.ChemicalReaction;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StartReactionUseCase {

    private final ChemicalReactionRepository reactionRepository;

    public StartReactionUseCase(ChemicalReactionRepository reactionRepository) {
        this.reactionRepository = reactionRepository;
    }

    public ChemicalReaction execute(UUID reactionId) {
        ChemicalReaction reaction = reactionRepository.findById(reactionId)
                .orElseThrow(() -> new ReactionNotFoundException(reactionId));

        reaction.start();
        return reactionRepository.save(reaction);
    }
}
