package com.luxoft.spingsecurity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class OrderDto {

    private long id;

    private double amount;
}
