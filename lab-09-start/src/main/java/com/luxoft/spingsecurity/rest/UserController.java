package com.luxoft.spingsecurity.rest;

import com.luxoft.spingsecurity.dto.UserDto;
import com.luxoft.spingsecurity.dto.converters.UserDtoConverter;
import com.luxoft.spingsecurity.model.User;
import com.luxoft.spingsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserDtoConverter userDtoConverter;
    private final UserService userService;

    @GetMapping("/user")
    public List<UserDto> getAll() {
        return userService.getAll()
                .stream()
                .map(userDtoConverter::toDto)
                .toList();
    }

    @GetMapping("/user/{id}")
    public UserDto getById(@PathVariable("id") long userId) {
        User user = userService.getById(userId);
        return userDtoConverter.toDto(user);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/user")
    public UserDto create(@RequestBody UserDto userDto) {
        var user = userDtoConverter.toDomain(userDto);
        User userSave = userService.create(user);
        return userDtoConverter.toDto(userSave);
    }

    @PutMapping("/user")
    public UserDto update(@RequestBody UserDto userDto) {
        var user = userDtoConverter.toDomain(userDto);
        User userUpdate = userService.update(user);
        return userDtoConverter.toDto(userUpdate);
    }
}
