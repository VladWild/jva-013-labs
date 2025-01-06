package com.luxoft.spingsecurity.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@SequenceGenerator(
    name = "company_seq_gen",
    sequenceName = "company_seq",
    initialValue = 1010
)
@Entity
@Data
public class Company {

    @EqualsAndHashCode.Include
    @GeneratedValue(generator = "company_seq_gen")
    @Id
    private long id;

    private String name;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders;
}
