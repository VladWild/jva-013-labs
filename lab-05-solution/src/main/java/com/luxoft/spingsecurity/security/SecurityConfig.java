package com.luxoft.spingsecurity.security;

import com.luxoft.spingsecurity.model.User;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import java.util.Collections;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .sessionManagement()
            //.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/login.html", "/login", "/deny.html").permitAll()
            .antMatchers("/user/whoami").permitAll()
            .antMatchers("/company/**", "/user/**").authenticated()
            .antMatchers("/info").hasAuthority("ROLE_ANON")
            .antMatchers("/**").denyAll()
            .and()
            .formLogin()
            .loginPage("/login.html")
            .loginProcessingUrl("/login")
            .failureUrl("/deny.html")
            .defaultSuccessUrl("/company", true)
            .and()
            .anonymous()
            // uncomment this, comment principal and try to GET /user/whoami
            // .authorities("ROLE_ANON");
            .principal(new UserDetailsAdapter(anonymous()));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            // TODO: replace by BCryptPasswordEncoder
            .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    private static User anonymous() {
        val user = new User();
        user.setId(-1);
        user.setLogin("anonymous");
        user.setPassword("");
        user.setRoles(Collections.singletonList("ROLE_ANON"));
        return user;
    }
}
