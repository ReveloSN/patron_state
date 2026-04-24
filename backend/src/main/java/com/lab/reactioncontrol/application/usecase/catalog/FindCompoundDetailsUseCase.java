package com.lab.reactioncontrol.application.usecase.catalog;

import com.lab.reactioncontrol.domain.catalog.CompoundCatalog;
import com.lab.reactioncontrol.domain.catalog.model.CompoundDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FindCompoundDetailsUseCase {

    private final CompoundCatalog compoundCatalog;

    public FindCompoundDetailsUseCase(CompoundCatalog compoundCatalog) {
        this.compoundCatalog = compoundCatalog;
    }

    public Optional<CompoundDetails> execute(String externalId) {
        return compoundCatalog.findByExternalId(externalId);
    }
}
