package com.lab.reactioncontrol.presentation.controller;

import com.lab.reactioncontrol.application.usecase.AddReactantUseCase;
import com.lab.reactioncontrol.application.usecase.CancelReactionUseCase;
import com.lab.reactioncontrol.application.usecase.CompleteReactionUseCase;
import com.lab.reactioncontrol.application.usecase.CreateReactionUseCase;
import com.lab.reactioncontrol.application.usecase.GetReactionByIdUseCase;
import com.lab.reactioncontrol.application.usecase.PauseReactionUseCase;
import com.lab.reactioncontrol.application.usecase.RecordMeasurementUseCase;
import com.lab.reactioncontrol.application.usecase.ResumeReactionUseCase;
import com.lab.reactioncontrol.application.usecase.StabilizeReactionUseCase;
import com.lab.reactioncontrol.application.usecase.StartReactionUseCase;
import com.lab.reactioncontrol.application.usecase.UpdateInitialConditionUseCase;
import com.lab.reactioncontrol.domain.model.Reactant;
import com.lab.reactioncontrol.domain.model.ReactionCondition;
import com.lab.reactioncontrol.domain.model.ReactionMeasurement;
import com.lab.reactioncontrol.presentation.dto.request.AddReactantRequest;
import com.lab.reactioncontrol.presentation.dto.request.CreateReactionRequest;
import com.lab.reactioncontrol.presentation.dto.request.RecordMeasurementRequest;
import com.lab.reactioncontrol.presentation.dto.request.StabilizeReactionRequest;
import com.lab.reactioncontrol.presentation.dto.request.UpdateInitialConditionRequest;
import com.lab.reactioncontrol.presentation.dto.response.ReactionResponse;
import com.lab.reactioncontrol.presentation.mapper.ReactionResponseMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/reactions")
public class ChemicalReactionController {

    private final CreateReactionUseCase createReactionUseCase;
    private final GetReactionByIdUseCase getReactionByIdUseCase;
    private final AddReactantUseCase addReactantUseCase;
    private final UpdateInitialConditionUseCase updateInitialConditionUseCase;
    private final StartReactionUseCase startReactionUseCase;
    private final RecordMeasurementUseCase recordMeasurementUseCase;
    private final PauseReactionUseCase pauseReactionUseCase;
    private final ResumeReactionUseCase resumeReactionUseCase;
    private final StabilizeReactionUseCase stabilizeReactionUseCase;
    private final CompleteReactionUseCase completeReactionUseCase;
    private final CancelReactionUseCase cancelReactionUseCase;
    private final ReactionResponseMapper responseMapper;

    public ChemicalReactionController(
            CreateReactionUseCase createReactionUseCase,
            GetReactionByIdUseCase getReactionByIdUseCase,
            AddReactantUseCase addReactantUseCase,
            UpdateInitialConditionUseCase updateInitialConditionUseCase,
            StartReactionUseCase startReactionUseCase,
            RecordMeasurementUseCase recordMeasurementUseCase,
            PauseReactionUseCase pauseReactionUseCase,
            ResumeReactionUseCase resumeReactionUseCase,
            StabilizeReactionUseCase stabilizeReactionUseCase,
            CompleteReactionUseCase completeReactionUseCase,
            CancelReactionUseCase cancelReactionUseCase,
            ReactionResponseMapper responseMapper
    ) {
        this.createReactionUseCase = createReactionUseCase;
        this.getReactionByIdUseCase = getReactionByIdUseCase;
        this.addReactantUseCase = addReactantUseCase;
        this.updateInitialConditionUseCase = updateInitialConditionUseCase;
        this.startReactionUseCase = startReactionUseCase;
        this.recordMeasurementUseCase = recordMeasurementUseCase;
        this.pauseReactionUseCase = pauseReactionUseCase;
        this.resumeReactionUseCase = resumeReactionUseCase;
        this.stabilizeReactionUseCase = stabilizeReactionUseCase;
        this.completeReactionUseCase = completeReactionUseCase;
        this.cancelReactionUseCase = cancelReactionUseCase;
        this.responseMapper = responseMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReactionResponse createReaction(@Valid @RequestBody CreateReactionRequest request) {
        return responseMapper.toResponse(
                createReactionUseCase.execute(
                        request.name(),
                        new ReactionCondition(
                                request.temperatureCelsius(),
                                request.pressureAtm(),
                                request.productConcentrationPercentage()
                        ),
                        request.catalyst()
                )
        );
    }

    @GetMapping("/{reactionId}")
    public ReactionResponse getReactionById(@PathVariable UUID reactionId) {
        return responseMapper.toResponse(getReactionByIdUseCase.execute(reactionId));
    }

    @PostMapping("/{reactionId}/reactants")
    public ReactionResponse addReactant(
            @PathVariable UUID reactionId,
            @Valid @RequestBody AddReactantRequest request
    ) {
        return responseMapper.toResponse(
                addReactantUseCase.execute(
                        reactionId,
                        new Reactant(request.name(), request.quantity(), request.unit())
                )
        );
    }

    @PutMapping("/{reactionId}/conditions")
    public ReactionResponse updateInitialCondition(
            @PathVariable UUID reactionId,
            @Valid @RequestBody UpdateInitialConditionRequest request
    ) {
        return responseMapper.toResponse(
                updateInitialConditionUseCase.execute(
                        reactionId,
                        new ReactionCondition(
                                request.temperatureCelsius(),
                                request.pressureAtm(),
                                request.productConcentrationPercentage()
                        ),
                        request.catalyst()
                )
        );
    }

    @PostMapping("/{reactionId}/start")
    public ReactionResponse startReaction(@PathVariable UUID reactionId) {
        return responseMapper.toResponse(startReactionUseCase.execute(reactionId));
    }

    @PostMapping("/{reactionId}/measurements")
    public ReactionResponse recordMeasurement(
            @PathVariable UUID reactionId,
            @Valid @RequestBody RecordMeasurementRequest request
    ) {
        return responseMapper.toResponse(
                recordMeasurementUseCase.execute(
                        reactionId,
                        ReactionMeasurement.now(
                                request.temperatureCelsius(),
                                request.pressureAtm(),
                                request.productConcentrationPercentage()
                        )
                )
        );
    }

    @PostMapping("/{reactionId}/pause")
    public ReactionResponse pauseReaction(@PathVariable UUID reactionId) {
        return responseMapper.toResponse(pauseReactionUseCase.execute(reactionId));
    }

    @PostMapping("/{reactionId}/resume")
    public ReactionResponse resumeReaction(@PathVariable UUID reactionId) {
        return responseMapper.toResponse(resumeReactionUseCase.execute(reactionId));
    }

    @PostMapping("/{reactionId}/stabilize")
    public ReactionResponse stabilizeReaction(
            @PathVariable UUID reactionId,
            @Valid @RequestBody StabilizeReactionRequest request
    ) {
        return responseMapper.toResponse(
                stabilizeReactionUseCase.execute(
                        reactionId,
                        ReactionMeasurement.now(
                                request.temperatureCelsius(),
                                request.pressureAtm(),
                                request.productConcentrationPercentage()
                        )
                )
        );
    }

    @PostMapping("/{reactionId}/complete")
    public ReactionResponse completeReaction(@PathVariable UUID reactionId) {
        return responseMapper.toResponse(completeReactionUseCase.execute(reactionId));
    }

    @PostMapping("/{reactionId}/cancel")
    public ReactionResponse cancelReaction(@PathVariable UUID reactionId) {
        return responseMapper.toResponse(cancelReactionUseCase.execute(reactionId));
    }
}
