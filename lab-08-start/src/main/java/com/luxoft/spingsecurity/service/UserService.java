package com.luxoft.spingsecurity.service;

import com.luxoft.spingsecurity.model.User;
import com.luxoft.spingsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @Transactional(readOnly = true)
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getById(long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User does not exist"));
    }

    @Transactional
    public User create(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User update(User user) {
        var userDataBase = userRepository.findById(user.getId())
            .orElseThrow(() -> new IllegalArgumentException("User does not exist"));
        userDataBase.setLogin(user.getLogin());
        userDataBase.setPassword(user.getPassword());
        userDataBase.setRoles(user.getRoles());
        return userRepository.save(userDataBase);
    }

    @PreFilter("filterObject.login == authentication.name")
    public List<User> getUsersAuthenticationList(List<User> users) {
        log.info("Users in method: {}", users);
        return users;
    }
}
