package com.lab.reactioncontrol.infrastructure.catalog;

import com.lab.reactioncontrol.domain.catalog.CompoundCatalog;
import com.lab.reactioncontrol.domain.catalog.model.CompoundDetails;
import com.lab.reactioncontrol.domain.catalog.model.CompoundIdentifier;
import com.lab.reactioncontrol.domain.catalog.model.CompoundRole;
import com.lab.reactioncontrol.domain.catalog.model.CompoundSource;
import com.lab.reactioncontrol.domain.catalog.model.CompoundSummary;
import com.lab.reactioncontrol.infrastructure.config.CatalogProperties;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HybridCompoundCatalogTest {

    @Test
    void shouldFallbackToLocalResultsWhenExternalCatalogIsUnavailable() {
        CatalogProperties properties = new CatalogProperties();
        properties.setEnabled(true);

        CompoundCatalog localCatalog = new StubCompoundCatalog(
                List.of(new CompoundSummary("local:water", "Water", "H2O", 18.02, CompoundRole.PRODUCT, CompoundSource.LOCAL)),
                Optional.empty()
        );

        CompoundCatalog externalCatalog = new CompoundCatalog() {
            @Override
            public List<CompoundSummary> searchByName(String query) {
                throw new ExternalCatalogUnavailableException("PubChem unavailable");
            }

            @Override
            public Optional<CompoundDetails> findByExternalId(String externalId) {
                throw new ExternalCatalogUnavailableException("PubChem unavailable");
            }
        };

        HybridCompoundCatalog hybridCompoundCatalog = new HybridCompoundCatalog(localCatalog, externalCatalog, properties);

        List<CompoundSummary> results = hybridCompoundCatalog.searchByName("water");

        assertEquals(1, results.size());
        assertEquals("LOCAL", results.getFirst().source().name());
    }

    @Test
    void shouldResolveLocalIdentifiersWithoutCallingExternalCatalog() {
        CatalogProperties properties = new CatalogProperties();
        properties.setEnabled(true);

        CompoundDetails localDetails = new CompoundDetails(
                "local:ethanol",
                "Ethanol",
                "C2H6O",
                46.07,
                List.of("Ethyl Alcohol"),
                CompoundRole.REACTANT,
                CompoundSource.LOCAL,
                List.of(new CompoundIdentifier("Local Catalog ID", "local:ethanol"))
        );

        CompoundCatalog localCatalog = new StubCompoundCatalog(List.of(localDetails.toSummary()), Optional.of(localDetails));
        CompoundCatalog externalCatalog = new StubCompoundCatalog(List.of(), Optional.empty());

        HybridCompoundCatalog hybridCompoundCatalog = new HybridCompoundCatalog(localCatalog, externalCatalog, properties);

        Optional<CompoundDetails> result = hybridCompoundCatalog.findByExternalId("local:ethanol");

        assertTrue(result.isPresent());
        assertEquals("Ethanol", result.get().name());
    }

    private record StubCompoundCatalog(
            List<CompoundSummary> summaries,
            Optional<CompoundDetails> details
    ) implements CompoundCatalog {

        @Override
        public List<CompoundSummary> searchByName(String query) {
            return summaries;
        }

        @Override
        public Optional<CompoundDetails> findByExternalId(String externalId) {
            return details;
        }
    }
}
