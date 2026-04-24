package com.lab.reactioncontrol.domain.model;

import com.lab.reactioncontrol.domain.exception.ReactionValidationException;
import com.lab.reactioncontrol.domain.phase.PreparedPhase;
import com.lab.reactioncontrol.domain.phase.ReactionPhase;
import com.lab.reactioncontrol.domain.policy.ReactionSafetyPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChemicalReaction {

    private final UUID id;
    private final String name;
    private final List<Reactant> reactants;
    private final List<ReactionMeasurement> measurements;
    private ReactionCondition currentCondition;
    private String catalyst;
    private ReactionPhase phase;

    private ChemicalReaction(
            UUID id,
            String name,
            ReactionCondition currentCondition,
            String catalyst,
            ReactionPhase phase
    ) {
        this.id = id;
        this.name = validateName(name);
        this.currentCondition = validateCondition(currentCondition);
        this.catalyst = normalizeCatalyst(catalyst);
        this.phase = validatePhase(phase);
        this.reactants = new ArrayList<>();
        this.measurements = new ArrayList<>();
    }

    public static ChemicalReaction create(String name, ReactionCondition initialCondition, String catalyst) {
        return new ChemicalReaction(UUID.randomUUID(), name, initialCondition, catalyst, new PreparedPhase());
    }

    public void addReactant(Reactant reactant) {
        phase.addReactant(this, reactant);
    }

    public void updateInitialCondition(ReactionCondition newCondition, String newCatalyst) {
        phase.updateInitialCondition(this, newCondition, newCatalyst);
    }

    public void start() {
        phase.start(this);
    }

    public void recordMeasurement(ReactionMeasurement measurement, ReactionSafetyPolicy safetyPolicy) {
        phase.recordMeasurement(this, measurement, safetyPolicy);
    }

    public void pause() {
        phase.pause(this);
    }

    public void resume() {
        phase.resume(this);
    }

    public void stabilize(ReactionMeasurement safeMeasurement, ReactionSafetyPolicy safetyPolicy) {
        phase.stabilize(this, safeMeasurement, safetyPolicy);
    }

    public void complete() {
        phase.complete(this);
    }

    public void cancel() {
        phase.cancel(this);
    }

    public void appendReactant(Reactant reactant) {
        reactants.add(reactant);
    }

    public void updateCurrentCondition(ReactionCondition newCondition) {
        currentCondition = validateCondition(newCondition);
    }

    public void updateCatalyst(String newCatalyst) {
        catalyst = normalizeCatalyst(newCatalyst);
    }

    public void appendMeasurement(ReactionMeasurement measurement) {
        measurements.add(measurement);
    }

    public void changePhase(ReactionPhase newPhase) {
        phase = validatePhase(newPhase);
    }

    public boolean hasReactants() {
        return !reactants.isEmpty();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Reactant> getReactants() {
        return List.copyOf(reactants);
    }

    public List<ReactionMeasurement> getMeasurements() {
        return List.copyOf(measurements);
    }

    public ReactionCondition getCurrentCondition() {
        return currentCondition;
    }

    public String getCatalyst() {
        return catalyst;
    }

    public ReactionPhase getPhase() {
        return phase;
    }

    public String getStatus() {
        return phase.getStatus();
    }

    public String getPhaseMessage() {
        return phase.getMessage();
    }

    private static String validateName(String reactionName) {
        if (reactionName == null || reactionName.isBlank()) {
            throw new ReactionValidationException("Reaction name is required.");
        }
        return reactionName;
    }

    private static ReactionCondition validateCondition(ReactionCondition reactionCondition) {
        if (reactionCondition == null) {
            throw new ReactionValidationException("Reaction condition is required.");
        }
        return reactionCondition;
    }

    private static ReactionPhase validatePhase(ReactionPhase reactionPhase) {
        if (reactionPhase == null) {
            throw new ReactionValidationException("Reaction phase is required.");
        }
        return reactionPhase;
    }

    private static String normalizeCatalyst(String catalystName) {
        return catalystName == null || catalystName.isBlank() ? null : catalystName;
    }
}
