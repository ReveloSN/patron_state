package com.lab.reactioncontrol.application.usecase;

import com.lab.reactioncontrol.application.port.out.ChemicalReactionRepository;
import com.lab.reactioncontrol.domain.exception.ReactionNotFoundException;
import com.lab.reactioncontrol.domain.model.ChemicalReaction;
import com.lab.reactioncontrol.domain.model.ReactionMeasurement;
import com.lab.reactioncontrol.domain.policy.ReactionSafetyPolicy;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RecordMeasurementUseCase {

    private final ChemicalReactionRepository reactionRepository;
    private final ReactionSafetyPolicy safetyPolicy;

    public RecordMeasurementUseCase(
            ChemicalReactionRepository reactionRepository,
            ReactionSafetyPolicy safetyPolicy
    ) {
        this.reactionRepository = reactionRepository;
        this.safetyPolicy = safetyPolicy;
    }

    public ChemicalReaction execute(UUID reactionId, ReactionMeasurement measurement) {
        ChemicalReaction reaction = reactionRepository.findById(reactionId)
                .orElseThrow(() -> new ReactionNotFoundException(reactionId));

        reaction.recordMeasurement(measurement, safetyPolicy);
        return reactionRepository.save(reaction);
    }
}
