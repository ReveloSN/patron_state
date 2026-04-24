package com.lab.reactioncontrol.infrastructure.catalog;

import com.lab.reactioncontrol.domain.catalog.CompoundCatalog;
import com.lab.reactioncontrol.domain.catalog.model.CompoundDetails;
import com.lab.reactioncontrol.domain.catalog.model.CompoundSummary;
import com.lab.reactioncontrol.infrastructure.config.CatalogProperties;

import java.util.List;
import java.util.Optional;

public class HybridCompoundCatalog implements CompoundCatalog {

    private final CompoundCatalog localCompoundCatalog;
    private final CompoundCatalog externalCompoundCatalog;
    private final CatalogProperties catalogProperties;

    public HybridCompoundCatalog(
            CompoundCatalog localCompoundCatalog,
            CompoundCatalog externalCompoundCatalog,
            CatalogProperties catalogProperties
    ) {
        this.localCompoundCatalog = localCompoundCatalog;
        this.externalCompoundCatalog = externalCompoundCatalog;
        this.catalogProperties = catalogProperties;
    }

    @Override
    public List<CompoundSummary> searchByName(String query) {
        if (query == null || query.isBlank()) {
            return localCompoundCatalog.searchByName(query);
        }

        if (!catalogProperties.isEnabled()) {
            return localCompoundCatalog.searchByName(query);
        }

        try {
            List<CompoundSummary> externalResults = externalCompoundCatalog.searchByName(query);
            if (!externalResults.isEmpty()) {
                return externalResults;
            }
        } catch (ExternalCatalogUnavailableException ignored) {
            return localCompoundCatalog.searchByName(query);
        }

        return localCompoundCatalog.searchByName(query);
    }

    @Override
    public Optional<CompoundDetails> findByExternalId(String externalId) {
        if (externalId == null || externalId.isBlank()) {
            return Optional.empty();
        }

        if (externalId.startsWith("local:") || !catalogProperties.isEnabled()) {
            return localCompoundCatalog.findByExternalId(externalId);
        }

        try {
            return externalCompoundCatalog.findByExternalId(externalId)
                    .or(() -> localCompoundCatalog.findByExternalId(externalId));
        } catch (ExternalCatalogUnavailableException ignored) {
            return localCompoundCatalog.findByExternalId(externalId);
        }
    }
}
