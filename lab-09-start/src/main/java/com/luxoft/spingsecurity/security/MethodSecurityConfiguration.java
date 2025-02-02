package com.luxoft.spingsecurity.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity(
        jsr250Enabled = false,
        prePostEnabled = true,
        securedEnabled = true
)
public class MethodSecurityConfiguration {

}

