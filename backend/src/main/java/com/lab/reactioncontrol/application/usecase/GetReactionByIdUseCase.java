package com.lab.reactioncontrol.application.usecase;

import com.lab.reactioncontrol.application.port.out.ChemicalReactionRepository;
import com.lab.reactioncontrol.domain.exception.ReactionNotFoundException;
import com.lab.reactioncontrol.domain.model.ChemicalReaction;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetReactionByIdUseCase {

    private final ChemicalReactionRepository reactionRepository;

    public GetReactionByIdUseCase(ChemicalReactionRepository reactionRepository) {
        this.reactionRepository = reactionRepository;
    }

    public ChemicalReaction execute(UUID reactionId) {
        return reactionRepository.findById(reactionId)
                .orElseThrow(() -> new ReactionNotFoundException(reactionId));
    }
}
