package com.lab.reactioncontrol.infrastructure.config;

import com.lab.reactioncontrol.domain.policy.ReactionSafetyPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {

    @Bean
    public ReactionSafetyPolicy reactionSafetyPolicy() {
        return new ReactionSafetyPolicy();
    }
}
