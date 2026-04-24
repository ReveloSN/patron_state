package com.lab.reactioncontrol.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab.reactioncontrol.domain.catalog.CompoundCatalog;
import com.lab.reactioncontrol.domain.catalog.ReactionRecipeCatalog;
import com.lab.reactioncontrol.domain.policy.ReactionSafetyPolicy;
import com.lab.reactioncontrol.infrastructure.catalog.HybridCompoundCatalog;
import com.lab.reactioncontrol.infrastructure.catalog.LocalCompoundCatalog;
import com.lab.reactioncontrol.infrastructure.catalog.LocalReactionRecipeCatalog;
import com.lab.reactioncontrol.infrastructure.catalog.PubChemCompoundCatalog;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CatalogProperties.class)
public class DomainConfiguration {

    @Bean
    public ReactionSafetyPolicy reactionSafetyPolicy() {
        return new ReactionSafetyPolicy();
    }

    @Bean
    public LocalCompoundCatalog localCompoundCatalog() {
        return new LocalCompoundCatalog();
    }

    @Bean
    public PubChemCompoundCatalog pubChemCompoundCatalog(
            CatalogProperties catalogProperties,
            ObjectMapper objectMapper
    ) {
        return new PubChemCompoundCatalog(catalogProperties, objectMapper);
    }

    @Bean
    public CompoundCatalog compoundCatalog(
            LocalCompoundCatalog localCompoundCatalog,
            PubChemCompoundCatalog pubChemCompoundCatalog,
            CatalogProperties catalogProperties
    ) {
        return new HybridCompoundCatalog(localCompoundCatalog, pubChemCompoundCatalog, catalogProperties);
    }

    @Bean
    public ReactionRecipeCatalog reactionRecipeCatalog() {
        return new LocalReactionRecipeCatalog();
    }
}
