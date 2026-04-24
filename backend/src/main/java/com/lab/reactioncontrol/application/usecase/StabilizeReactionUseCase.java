package com.lab.reactioncontrol.application.usecase;

import com.lab.reactioncontrol.application.port.out.ChemicalReactionRepository;
import com.lab.reactioncontrol.domain.exception.ReactionNotFoundException;
import com.lab.reactioncontrol.domain.model.ChemicalReaction;
import com.lab.reactioncontrol.domain.model.ReactionMeasurement;
import com.lab.reactioncontrol.domain.policy.ReactionSafetyPolicy;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StabilizeReactionUseCase {

    private final ChemicalReactionRepository reactionRepository;
    private final ReactionSafetyPolicy safetyPolicy;

    public StabilizeReactionUseCase(
            ChemicalReactionRepository reactionRepository,
            ReactionSafetyPolicy safetyPolicy
    ) {
        this.reactionRepository = reactionRepository;
        this.safetyPolicy = safetyPolicy;
    }

    public ChemicalReaction execute(UUID reactionId, ReactionMeasurement safeMeasurement) {
        ChemicalReaction reaction = reactionRepository.findById(reactionId)
                .orElseThrow(() -> new ReactionNotFoundException(reactionId));

        reaction.stabilize(safeMeasurement, safetyPolicy);
        return reactionRepository.save(reaction);
    }
}
