package com.psc.personal_salary_calculator.dto;

import com.psc.personal_salary_calculator.domain.Country;

public record SupportedCountryResponse(
        String countryCode,
        String currency,
        String countryName
) {
    public static SupportedCountryResponse from(Country country) {
        return new SupportedCountryResponse(
                country.getCountryCode(),
                country.getCurrencyCode(),
                country.getCountryName()
        );
    }
}