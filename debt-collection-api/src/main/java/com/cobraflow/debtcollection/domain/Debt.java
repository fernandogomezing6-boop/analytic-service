package com.cobraflow.debtcollection.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "debts")
@Getter
@Setter
public class Debt {
    @Id
    private Long id;

    @Column(nullable = false)
    private String debtor;

    @Column(nullable = false)
    private String document;

    @Column(nullable = false)
    private String concept;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private Integer days;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DebtState state;

    @ManyToOne(fetch = FetchType.LAZY)
    private Agent agent;
}