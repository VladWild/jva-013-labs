package com.luxoft.spingsecurity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserDto {

    private long id;

    private String login;

    private String password;

    private List<String> roles;
}
