package com.lab.reactioncontrol.infrastructure.catalog;

import com.lab.reactioncontrol.domain.catalog.model.CompoundDetails;
import com.lab.reactioncontrol.domain.catalog.model.CompoundSummary;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocalCompoundCatalogTest {

    private final LocalCompoundCatalog localCompoundCatalog = new LocalCompoundCatalog();

    @Test
    void shouldSearchLocalCompoundsByNameOrSynonym() {
        List<CompoundSummary> results = localCompoundCatalog.searchByName("ethyl alcohol");

        assertEquals(1, results.size());
        assertEquals("Ethanol", results.getFirst().name());
    }

    @Test
    void shouldReturnCompoundDetailsByLocalIdentifier() {
        Optional<CompoundDetails> result = localCompoundCatalog.findByExternalId("local:acetic-acid");

        assertTrue(result.isPresent());
        assertEquals("Acetic Acid", result.get().name());
        assertEquals("C2H4O2", result.get().molecularFormula());
    }
}
