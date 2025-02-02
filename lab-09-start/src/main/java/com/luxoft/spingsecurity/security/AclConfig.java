package com.luxoft.spingsecurity.security;

import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl;
import org.springframework.security.acls.domain.ConsoleAuditLogger;
import org.springframework.security.acls.domain.DefaultPermissionFactory;
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy;
import org.springframework.security.acls.domain.SpringCacheBasedAclCache;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@AllArgsConstructor
public class AclConfig {

    private final DataSource dataSource;

    @Bean
    public CacheManager cacheManager() {
        final SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(List.of(new ConcurrentMapCache("security/acl")));
        return cacheManager;
    }

    @Bean
    public MutableAclService mutableAclService(CacheManager cacheManager) {
        var aclAuthorizationStrategy = new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN"));
        var auditLogger = new ConsoleAuditLogger();
        var permissionGrantingStrategy = new DefaultPermissionGrantingStrategy(auditLogger);
        var aclCache = new SpringCacheBasedAclCache(cacheManager.getCache("security/acl"),
                permissionGrantingStrategy, aclAuthorizationStrategy);
        var lookupStrategy = new BasicLookupStrategy(dataSource, aclCache,
                aclAuthorizationStrategy, permissionGrantingStrategy);
        return new JdbcMutableAclService(dataSource, lookupStrategy, aclCache);
    }

    @Bean
    public DefaultPermissionFactory permissionFactory() {
        return new DefaultPermissionFactory();
    }

    @Bean
    public AclPermissionEvaluator permissionEvaluator(MutableAclService aclService,
                                                      DefaultPermissionFactory permissionFactory) {
        AclPermissionEvaluator aclPermissionEvaluator = new AclPermissionEvaluator(aclService);
        aclPermissionEvaluator.setPermissionFactory(permissionFactory);
        return aclPermissionEvaluator;
    }

    @Bean
    public DefaultMethodSecurityExpressionHandler methodSecurityExpressionHandler(AclPermissionEvaluator permissionEvaluator,
                                                                                  ApplicationContext applicationContext) {
        var defaultHttpSecurityExpressionHandler = new DefaultMethodSecurityExpressionHandler();
        defaultHttpSecurityExpressionHandler.setPermissionEvaluator(permissionEvaluator);
        defaultHttpSecurityExpressionHandler.setApplicationContext(applicationContext);
        return defaultHttpSecurityExpressionHandler;
    }
}
