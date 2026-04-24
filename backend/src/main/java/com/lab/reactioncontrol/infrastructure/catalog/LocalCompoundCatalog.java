package com.lab.reactioncontrol.infrastructure.catalog;

import com.lab.reactioncontrol.domain.catalog.CompoundCatalog;
import com.lab.reactioncontrol.domain.catalog.model.CompoundDetails;
import com.lab.reactioncontrol.domain.catalog.model.CompoundIdentifier;
import com.lab.reactioncontrol.domain.catalog.model.CompoundRole;
import com.lab.reactioncontrol.domain.catalog.model.CompoundSource;
import com.lab.reactioncontrol.domain.catalog.model.CompoundSummary;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class LocalCompoundCatalog implements CompoundCatalog {

    private final List<CompoundDetails> compounds = List.of(
            localCompound(
                    "acetic-acid",
                    "Acetic Acid",
                    "C2H4O2",
                    60.05,
                    CompoundRole.REACTANT,
                    List.of("Ethanoic Acid", "Vinegar Acid")
            ),
            localCompound(
                    "ethanol",
                    "Ethanol",
                    "C2H6O",
                    46.07,
                    CompoundRole.REACTANT,
                    List.of("Ethyl Alcohol", "Alcohol")
            ),
            localCompound(
                    "ethyl-acetate",
                    "Ethyl Acetate",
                    "C4H8O2",
                    88.11,
                    CompoundRole.PRODUCT,
                    List.of("Acetic Acid Ethyl Ester")
            ),
            localCompound(
                    "sulfuric-acid",
                    "Sulfuric Acid",
                    "H2SO4",
                    98.08,
                    CompoundRole.CATALYST,
                    List.of("Oil of Vitriol")
            ),
            localCompound(
                    "hydrochloric-acid",
                    "Hydrochloric Acid",
                    "HCl",
                    36.46,
                    CompoundRole.REACTANT,
                    List.of("Muriatic Acid")
            ),
            localCompound(
                    "sodium-hydroxide",
                    "Sodium Hydroxide",
                    "NaOH",
                    40.00,
                    CompoundRole.REACTANT,
                    List.of("Caustic Soda", "Lye")
            ),
            localCompound(
                    "sodium-chloride",
                    "Sodium Chloride",
                    "NaCl",
                    58.44,
                    CompoundRole.PRODUCT,
                    List.of("Table Salt", "Halite")
            ),
            localCompound(
                    "water",
                    "Water",
                    "H2O",
                    18.02,
                    CompoundRole.PRODUCT,
                    List.of("Dihydrogen Monoxide")
            ),
            localCompound(
                    "hydrogen-peroxide",
                    "Hydrogen Peroxide",
                    "H2O2",
                    34.01,
                    CompoundRole.REACTANT,
                    List.of("Peroxide")
            ),
            localCompound(
                    "oxygen",
                    "Oxygen",
                    "O2",
                    32.00,
                    CompoundRole.PRODUCT,
                    List.of("Dioxygen")
            ),
            localCompound(
                    "manganese-dioxide",
                    "Manganese Dioxide",
                    "MnO2",
                    86.94,
                    CompoundRole.CATALYST,
                    List.of("Manganese(IV) Oxide")
            )
    );

    @Override
    public List<CompoundSummary> searchByName(String query) {
        String normalizedQuery = normalize(query);

        return compounds.stream()
                .filter(compound -> matches(compound, normalizedQuery))
                .sorted(Comparator
                        .comparing((CompoundDetails compound) -> startsWith(compound, normalizedQuery)).reversed()
                        .thenComparing(CompoundDetails::name))
                .map(CompoundDetails::toSummary)
                .toList();
    }

    @Override
    public Optional<CompoundDetails> findByExternalId(String externalId) {
        if (externalId == null || externalId.isBlank()) {
            return Optional.empty();
        }

        return compounds.stream()
                .filter(compound -> compound.externalId().equalsIgnoreCase(externalId))
                .findFirst();
    }

    private boolean matches(CompoundDetails compound, String query) {
        if (query.isBlank()) {
            return true;
        }

        return normalize(compound.name()).contains(query)
                || normalize(compound.molecularFormula()).contains(query)
                || compound.synonyms().stream().map(this::normalize).anyMatch(synonym -> synonym.contains(query));
    }

    private boolean startsWith(CompoundDetails compound, String query) {
        if (query.isBlank()) {
            return true;
        }

        return normalize(compound.name()).startsWith(query)
                || compound.synonyms().stream().map(this::normalize).anyMatch(synonym -> synonym.startsWith(query));
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

    private static CompoundDetails localCompound(
            String slug,
            String name,
            String molecularFormula,
            double molecularWeight,
            CompoundRole primaryRole,
            List<String> synonyms
    ) {
        String localId = "local:" + slug;

        return new CompoundDetails(
                localId,
                name,
                molecularFormula,
                molecularWeight,
                List.copyOf(synonyms),
                primaryRole,
                CompoundSource.LOCAL,
                List.of(new CompoundIdentifier("Local Catalog ID", localId))
        );
    }
}
