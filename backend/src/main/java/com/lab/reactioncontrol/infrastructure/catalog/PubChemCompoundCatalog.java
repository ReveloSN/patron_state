package com.lab.reactioncontrol.infrastructure.catalog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab.reactioncontrol.domain.catalog.CompoundCatalog;
import com.lab.reactioncontrol.domain.catalog.model.CompoundDetails;
import com.lab.reactioncontrol.domain.catalog.model.CompoundIdentifier;
import com.lab.reactioncontrol.domain.catalog.model.CompoundSource;
import com.lab.reactioncontrol.domain.catalog.model.CompoundSummary;
import com.lab.reactioncontrol.infrastructure.config.CatalogProperties;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PubChemCompoundCatalog implements CompoundCatalog {

    private final CatalogProperties catalogProperties;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public PubChemCompoundCatalog(CatalogProperties catalogProperties, ObjectMapper objectMapper) {
        this.catalogProperties = catalogProperties;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(catalogProperties.getConnectTimeout())
                .build();
    }

    @Override
    public List<CompoundSummary> searchByName(String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }

        List<Long> cids = fetchCompoundIds(query);
        if (cids.isEmpty()) {
            return List.of();
        }

        String cidList = cids.stream()
                .limit(catalogProperties.getMaxSearchResults())
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        URI uri = URI.create(
                catalogProperties.getBaseUrl()
                        + "/compound/cid/"
                        + cidList
                        + "/property/Title,IUPACName,MolecularFormula,MolecularWeight/JSON"
        );

        PropertyTableResponse response = readJson(uri, PropertyTableResponse.class, false);
        if (response.propertyTable() == null || response.propertyTable().properties() == null) {
            return List.of();
        }

        return response.propertyTable().properties().stream()
                .map(this::toSummary)
                .toList();
    }

    @Override
    public Optional<CompoundDetails> findByExternalId(String externalId) {
        String normalizedExternalId = normalizeExternalId(externalId);
        if (normalizedExternalId == null) {
            return Optional.empty();
        }

        URI propertyUri = URI.create(
                catalogProperties.getBaseUrl()
                        + "/compound/cid/"
                        + normalizedExternalId
                        + "/property/Title,IUPACName,MolecularFormula,MolecularWeight,InChI,InChIKey,CanonicalSMILES/JSON"
        );

        PropertyTableResponse propertyResponse = readJson(propertyUri, PropertyTableResponse.class, true);
        if (propertyResponse == null
                || propertyResponse.propertyTable() == null
                || propertyResponse.propertyTable().properties() == null
                || propertyResponse.propertyTable().properties().isEmpty()) {
            return Optional.empty();
        }

        PropertyRecord propertyRecord = propertyResponse.propertyTable().properties().getFirst();

        URI synonymUri = URI.create(
                catalogProperties.getBaseUrl()
                        + "/compound/cid/"
                        + normalizedExternalId
                        + "/synonyms/JSON"
        );

        SynonymResponse synonymResponse = readJson(synonymUri, SynonymResponse.class, true);
        List<String> synonyms = extractSynonyms(synonymResponse);

        return Optional.of(new CompoundDetails(
                pubChemExternalId(propertyRecord.cid()),
                resolveName(propertyRecord),
                propertyRecord.molecularFormula(),
                safeMolecularWeight(propertyRecord.molecularWeight()),
                synonyms,
                null,
                CompoundSource.PUBCHEM,
                buildIdentifiers(propertyRecord)
        ));
    }

    private List<Long> fetchCompoundIds(String query) {
        URI uri = URI.create(
                catalogProperties.getBaseUrl()
                        + "/compound/name/"
                        + encodePath(query)
                        + "/cids/JSON"
        );

        IdentifierListResponse response = readJson(uri, IdentifierListResponse.class, true);
        if (response == null || response.identifierList() == null || response.identifierList().cid() == null) {
            return List.of();
        }

        return response.identifierList().cid();
    }

    private CompoundSummary toSummary(PropertyRecord propertyRecord) {
        return new CompoundSummary(
                pubChemExternalId(propertyRecord.cid()),
                resolveName(propertyRecord),
                propertyRecord.molecularFormula(),
                safeMolecularWeight(propertyRecord.molecularWeight()),
                null,
                CompoundSource.PUBCHEM
        );
    }

    private List<CompoundIdentifier> buildIdentifiers(PropertyRecord propertyRecord) {
        return List.of(
                new CompoundIdentifier("PubChem CID", String.valueOf(propertyRecord.cid())),
                new CompoundIdentifier("PubChem URL", "https://pubchem.ncbi.nlm.nih.gov/compound/" + propertyRecord.cid()),
                optionalIdentifier("InChIKey", propertyRecord.inChIKey()),
                optionalIdentifier("InChI", propertyRecord.inChI()),
                optionalIdentifier("Canonical SMILES", propertyRecord.canonicalSmiles())
        ).stream().filter(identifier -> identifier != null).toList();
    }

    private CompoundIdentifier optionalIdentifier(String label, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new CompoundIdentifier(label, value);
    }

    private List<String> extractSynonyms(SynonymResponse synonymResponse) {
        if (synonymResponse == null
                || synonymResponse.informationList() == null
                || synonymResponse.informationList().information() == null
                || synonymResponse.informationList().information().isEmpty()) {
            return List.of();
        }

        List<String> synonyms = synonymResponse.informationList().information().getFirst().synonym();
        if (synonyms == null) {
            return List.of();
        }

        return synonyms.stream()
                .filter(synonym -> synonym != null && !synonym.isBlank())
                .distinct()
                .limit(10)
                .toList();
    }

    private String resolveName(PropertyRecord propertyRecord) {
        if (propertyRecord.title() != null && !propertyRecord.title().isBlank()) {
            return propertyRecord.title();
        }
        if (propertyRecord.iupacName() != null && !propertyRecord.iupacName().isBlank()) {
            return propertyRecord.iupacName();
        }
        return "PubChem Compound " + propertyRecord.cid();
    }

    private double safeMolecularWeight(Double molecularWeight) {
        return molecularWeight == null ? 0.0 : molecularWeight;
    }

    private String normalizeExternalId(String externalId) {
        if (externalId == null || externalId.isBlank()) {
            return null;
        }

        if (externalId.startsWith("pubchem:")) {
            return externalId.substring("pubchem:".length());
        }

        return externalId.chars().allMatch(Character::isDigit) ? externalId : null;
    }

    private String pubChemExternalId(long cid) {
        return "pubchem:" + cid;
    }

    private String encodePath(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
    }

    private <T> T readJson(URI uri, Class<T> responseType, boolean allowNotFound) {
        HttpRequest request = HttpRequest.newBuilder(uri)
                .timeout(catalogProperties.getReadTimeout())
                .header("Accept", "application/json")
                .header("User-Agent", "reaction-control/1.0")
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 404 && allowNotFound) {
                return null;
            }
            if (response.statusCode() >= 400) {
                throw new ExternalCatalogUnavailableException(
                        "PubChem request failed with status " + response.statusCode() + "."
                );
            }

            return objectMapper.readValue(response.body(), responseType);
        } catch (IOException | InterruptedException exception) {
            if (exception instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new ExternalCatalogUnavailableException("PubChem catalog is unavailable.", exception);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record IdentifierListResponse(@JsonProperty("IdentifierList") IdentifierList identifierList) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record IdentifierList(@JsonProperty("CID") List<Long> cid) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record PropertyTableResponse(@JsonProperty("PropertyTable") PropertyTable propertyTable) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record PropertyTable(@JsonProperty("Properties") List<PropertyRecord> properties) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record PropertyRecord(
            @JsonProperty("CID") long cid,
            @JsonProperty("Title") String title,
            @JsonProperty("IUPACName") String iupacName,
            @JsonProperty("MolecularFormula") String molecularFormula,
            @JsonProperty("MolecularWeight") Double molecularWeight,
            @JsonProperty("InChI") String inChI,
            @JsonProperty("InChIKey") String inChIKey,
            @JsonProperty("CanonicalSMILES") String canonicalSmiles
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record SynonymResponse(@JsonProperty("InformationList") InformationList informationList) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record InformationList(@JsonProperty("Information") List<SynonymInformation> information) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record SynonymInformation(@JsonProperty("Synonym") List<String> synonym) {
    }
}
