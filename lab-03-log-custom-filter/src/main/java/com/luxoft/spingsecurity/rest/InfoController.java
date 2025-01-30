package com.luxoft.spingsecurity.rest;

import jakarta.servlet.Filter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
public class InfoController {

    private final SecurityFilterChain securityFilterChain;

    @GetMapping("/info")
    public Map<String, String> getInfo() {
        List<Filter> filters = securityFilterChain.getFilters();
        log.info("Filters: {}", filters);
        return Collections.singletonMap("version", "1.0");
    }
}
