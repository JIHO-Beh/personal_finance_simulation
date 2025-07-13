package com.psc.personal_salary_calculator.repository;

import com.psc.personal_salary_calculator.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findByCountryCode(String countryCode);
}