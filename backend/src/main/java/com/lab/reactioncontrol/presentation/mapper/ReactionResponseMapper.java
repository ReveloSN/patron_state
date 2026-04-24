package com.lab.reactioncontrol.presentation.mapper;

import com.lab.reactioncontrol.domain.model.ChemicalReaction;
import com.lab.reactioncontrol.domain.model.Reactant;
import com.lab.reactioncontrol.domain.model.ReactionCondition;
import com.lab.reactioncontrol.domain.model.ReactionMeasurement;
import com.lab.reactioncontrol.presentation.dto.response.ReactantResponse;
import com.lab.reactioncontrol.presentation.dto.response.ReactionConditionResponse;
import com.lab.reactioncontrol.presentation.dto.response.ReactionMeasurementResponse;
import com.lab.reactioncontrol.presentation.dto.response.ReactionResponse;
import org.springframework.stereotype.Component;

@Component
public class ReactionResponseMapper {

    public ReactionResponse toResponse(ChemicalReaction reaction) {
        return new ReactionResponse(
                reaction.getId(),
                reaction.getName(),
                reaction.getStatus(),
                reaction.getPhaseMessage(),
                reaction.getCatalyst(),
                toConditionResponse(reaction.getCurrentCondition()),
                reaction.getReactants().stream().map(this::toReactantResponse).toList(),
                reaction.getMeasurements().stream().map(this::toMeasurementResponse).toList()
        );
    }

    private ReactantResponse toReactantResponse(Reactant reactant) {
        return new ReactantResponse(
                reactant.name(),
                reactant.quantity(),
                reactant.unit()
        );
    }

    private ReactionConditionResponse toConditionResponse(ReactionCondition condition) {
        return new ReactionConditionResponse(
                condition.temperatureCelsius(),
                condition.pressureAtm(),
                condition.productConcentrationPercentage()
        );
    }

    private ReactionMeasurementResponse toMeasurementResponse(ReactionMeasurement measurement) {
        return new ReactionMeasurementResponse(
                measurement.temperatureCelsius(),
                measurement.pressureAtm(),
                measurement.productConcentrationPercentage(),
                measurement.recordedAt()
        );
    }
}
