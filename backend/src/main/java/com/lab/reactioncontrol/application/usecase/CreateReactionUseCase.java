package com.lab.reactioncontrol.application.usecase;

import com.lab.reactioncontrol.application.port.out.ChemicalReactionRepository;
import com.lab.reactioncontrol.domain.model.ChemicalReaction;
import com.lab.reactioncontrol.domain.model.ReactionCondition;
import org.springframework.stereotype.Service;

@Service
public class CreateReactionUseCase {

    private final ChemicalReactionRepository reactionRepository;

    public CreateReactionUseCase(ChemicalReactionRepository reactionRepository) {
        this.reactionRepository = reactionRepository;
    }

    public ChemicalReaction execute(String name, ReactionCondition initialCondition, String catalyst) {
        ChemicalReaction reaction = ChemicalReaction.create(name, initialCondition, catalyst);
        return reactionRepository.save(reaction);
    }
}
