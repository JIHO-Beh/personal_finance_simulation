package com.psc.personal_salary_calculator.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "tax_rate")
@Getter
@NoArgsConstructor
public class TaxRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", unique = true, nullable = false)
    private Country country;

    @Column(name = "income_tax_rate", nullable = false)
    private BigDecimal incomeTaxRate;

    @Column(name = "social_security_rate", nullable = false)
    private BigDecimal socialSecurityRate;
}