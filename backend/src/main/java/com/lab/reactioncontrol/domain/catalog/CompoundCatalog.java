package com.lab.reactioncontrol.domain.catalog;

import com.lab.reactioncontrol.domain.catalog.model.CompoundDetails;
import com.lab.reactioncontrol.domain.catalog.model.CompoundSummary;

import java.util.List;
import java.util.Optional;

public interface CompoundCatalog {

    List<CompoundSummary> searchByName(String query);

    Optional<CompoundDetails> findByExternalId(String externalId);
}
