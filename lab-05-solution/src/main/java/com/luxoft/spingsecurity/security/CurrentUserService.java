package com.luxoft.spingsecurity.security;

import com.luxoft.spingsecurity.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * This service is very useful
 * - you can get current user (domain class) without static calls
 * - you can simple mock this service in unit-tests
 */
@Service
public class CurrentUserService {

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // For authenticated users Principal is an UserDetails object
        UserDetailsAdapter userDetails = (UserDetailsAdapter) auth.getPrincipal();
        return userDetails.getUser();
    }
}
