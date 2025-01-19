package com.luxoft.spingsecurity.security;

import com.luxoft.spingsecurity.model.User;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return webSecurity -> webSecurity.ignoring().requestMatchers("/h2-console/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                //.anonymous(AbstractHttpConfigurer::disable)
                /*.anonymous(
                        anon -> anon
                                .authorities("ROLE_ANON")
                                .principal("myAnonUser")
                )*/
                .anonymous(
                        anon -> anon
                                .principal(new UserDetailsAdapter(anonymousUser()))
                                .authorities((List<GrantedAuthority>) new UserDetailsAdapter(anonymousUser()).getAuthorities())
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(withDefaults())
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/login.html", "/login", "/deny.html"/*, "/user/whoami"*/).permitAll()
                                //TODO: HomeWork -> дать роль ROLE_NEW_USER_ANON всем пользователям
                                .requestMatchers("/user/whoami","/info").hasAuthority("ROLE_NEW_USER_ANON")
                                .requestMatchers("/company/**", "/user/**").authenticated()
                                .requestMatchers("/**").denyAll()
                )
                .formLogin(fl ->
                        fl.loginPage("/login.html")
                                .loginProcessingUrl("/login")
                                .failureUrl("/deny.html")
                                .defaultSuccessUrl("/company", true)
                )
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return authenticationProvider;
    }

    private User anonymousUser() {
        User user = new User();
        user.setId(-1);
        user.setLogin("newUserAnon");
        user.setPassword("");
        user.setRoles(Collections.singletonList("NEW_USER_ANON"));
        return user;
    }
}
