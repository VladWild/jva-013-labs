package com.luxoft.spingsecurity.service;

import com.luxoft.spingsecurity.model.Company;
import com.luxoft.spingsecurity.repository.CompanyRepository;
import com.luxoft.spingsecurity.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AclService {

    private final MutableAclService mutableAclService;

    public void createAcl(Company company) {
        MutableAcl acl = mutableAclService.createAcl(new ObjectIdentityImpl(company));
        acl.insertAce(
                0,
                BasePermission.READ,
                new PrincipalSid(SecurityContextHolder.getContext().getAuthentication()),
                true
        );

        this.mutableAclService.updateAcl(acl);
    }
}
