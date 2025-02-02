package com.luxoft.spingsecurity.repository;

import com.luxoft.spingsecurity.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostFilter;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Company> findAll();
}
