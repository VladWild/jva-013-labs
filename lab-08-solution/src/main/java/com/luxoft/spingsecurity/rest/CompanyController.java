package com.luxoft.spingsecurity.rest;

import com.luxoft.spingsecurity.dto.CompanyDto;
import com.luxoft.spingsecurity.dto.OrderDto;
import com.luxoft.spingsecurity.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/company")
    public List<CompanyDto> getAll() {
        return companyService.getAll();
    }

    @GetMapping("/company/{id}")
    public CompanyDto getById(@PathVariable("id") long companyId) {
        return companyService.getById(companyId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/company")
    public CompanyDto create(
        @RequestBody CompanyDto newCompany,
        @RequestParam("user-id") long userId
    ) {
        return companyService.createCompany(newCompany, userId);
    }

    @PutMapping("/company")
    public List<CompanyDto> update(@RequestBody CompanyDto companyDto) {
        return companyService.updateCompany(Collections.singletonList(companyDto));
    }

    @GetMapping("/company/{id}/order")
    public List<OrderDto> getCompanyOrders(@PathVariable("id") long companyId) {
        return companyService.getCompanyOrders(companyId);
    }

    @PostMapping("/company/{id}/order")
    public OrderDto createOrder(
        @PathVariable("id") long companyId,
        @RequestBody OrderDto orderDto
    ) {
        return companyService.createOrder(companyId, orderDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/company/{cid}/order/{oid}")
    public void deleteOrder(
        @PathVariable("cid") long companyId,
        @PathVariable("oid") long orderId
    ) {
        companyService.deleteOrder(companyId, orderId);
    }
}
