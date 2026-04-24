package com.lab.reactioncontrol.application.usecase.catalog;

import com.lab.reactioncontrol.domain.catalog.CompoundCatalog;
import com.lab.reactioncontrol.domain.catalog.model.CompoundSummary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchCompoundsUseCase {

    private final CompoundCatalog compoundCatalog;

    public SearchCompoundsUseCase(CompoundCatalog compoundCatalog) {
        this.compoundCatalog = compoundCatalog;
    }

    public List<CompoundSummary> execute(String query) {
        return compoundCatalog.searchByName(query);
    }
}
