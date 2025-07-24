package com.psc.personal_salary_calculator.repository;

import com.psc.personal_salary_calculator.domain.Country;
import com.psc.personal_salary_calculator.domain.TaxRate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TaxRateRepository extends JpaRepository<TaxRate, Long> {
    Optional<TaxRate> findByCountry(Country country);
}