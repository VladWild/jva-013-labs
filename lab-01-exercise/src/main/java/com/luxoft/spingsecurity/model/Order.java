package com.luxoft.spingsecurity.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@SequenceGenerator(
    name = "order_seq_gen",
    sequenceName = "order_seq",
    initialValue = 2010
)
@Entity
@Data
@Table(name = "orders")
public class Order {

    @EqualsAndHashCode.Include
    @GeneratedValue(generator = "order_seq_gen")
    @Id
    private long id;

    private double amount;

    @ManyToOne
    private Company customer;
}
