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
        // Стратегия авторизации в ACL определяет, кто и как может изменять ACL глобально
        // В данной ситуации полные права предоставляются пользователям с правом ROLE_ADMIN
        var aclAuthorizationStrategy = new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN"));

        // Логгер аудита (обычный System.out.println(..))
        var auditLogger = new ConsoleAuditLogger();

        // Дефолтная стратегия авторизации
        var permissionGrantingStrategy = new DefaultPermissionGrantingStrategy(auditLogger);

        // Кэш ACL
        var aclCache = new SpringCacheBasedAclCache(cacheManager.getCache("security/acl"),
                permissionGrantingStrategy, aclAuthorizationStrategy);

        // Стратегия поиска ACL
        var lookupStrategy = new BasicLookupStrategy(dataSource, aclCache,
                aclAuthorizationStrategy, permissionGrantingStrategy);

        // Собственно ACL-сервис
        return new JdbcMutableAclService(dataSource, lookupStrategy, aclCache);
    }

    @Bean
    public DefaultPermissionFactory permissionFactory() {
        // Использование дефолтных разрешений из BasePermission (можно настроить свои через конструктор)
        return new DefaultPermissionFactory();
    }

    @Bean
    public AclPermissionEvaluator permissionEvaluator(MutableAclService aclService,
                                                      DefaultPermissionFactory permissionFactory) {
        // Компонент для определения прав в SPEL взамен стандартному DenyAppPermissionEvaluator
        AclPermissionEvaluator aclPermissionEvaluator = new AclPermissionEvaluator(aclService);
        aclPermissionEvaluator.setPermissionFactory(permissionFactory);
        return aclPermissionEvaluator;
    }

    @Bean
    public DefaultMethodSecurityExpressionHandler methodSecurityExpressionHandler(AclPermissionEvaluator permissionEvaluator,
                                                                           ApplicationContext applicationContext) {
        // Компонент для обработки SPEL-выражений для безопасности методов
        // В отличие от DefaultHttpSecurityExpressionHandler он внедряется сам в зависимые объекты
        var defaultHttpSecurityExpressionHandler = new DefaultMethodSecurityExpressionHandler();
        defaultHttpSecurityExpressionHandler.setPermissionEvaluator(permissionEvaluator);
        defaultHttpSecurityExpressionHandler.setApplicationContext(applicationContext);
        return defaultHttpSecurityExpressionHandler;
    }
}
