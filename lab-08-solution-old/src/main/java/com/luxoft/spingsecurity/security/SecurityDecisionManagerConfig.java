package com.luxoft.spingsecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Configuration
public class SecurityDecisionManagerConfig {

    @Bean
    public AccessDecisionManager accessUnanimousBased() {
        return new UnanimousBased(voters());
    }

    @Bean
    public AccessDecisionManager accessAffirmativeBased() {
        return new AffirmativeBased(voters());
    }

    private List<AccessDecisionVoter<?>> voters() {
        return Arrays.asList(
                new RoleVoter(),
                new AuthenticatedVoter(),
                new WebExpressionVoter(),
                createCustomVoter()
        );
    }

    private AccessDecisionVoter<?> createCustomVoter() {
        return new AccessDecisionVoter<>() {

            @Override
            public boolean supports(ConfigAttribute configAttribute) {
                return true;
            }

            @Override
            public boolean supports(Class<?> aClass) {
                return true;
            }

            @Override
            public int vote(Authentication a, Object o, Collection<ConfigAttribute> c) {
                //some custom logic
                return -1;
            }
        };
    }
}
