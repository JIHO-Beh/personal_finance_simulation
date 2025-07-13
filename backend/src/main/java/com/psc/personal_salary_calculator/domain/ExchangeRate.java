package com.psc.personal_salary_calculator.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "exchange_rate")
@Getter
@NoArgsConstructor
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "currency_code", unique = true, nullable = false, length = 3)
    private String currencyCode;

    @Column(name = "rate_to_usd", nullable = false)
    private BigDecimal rateToUsd;

    @Column(name = "last_updated_at")
    private OffsetDateTime lastUpdatedAt;
}