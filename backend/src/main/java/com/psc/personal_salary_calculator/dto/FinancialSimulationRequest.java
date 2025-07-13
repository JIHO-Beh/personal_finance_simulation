package com.psc.personal_salary_calculator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record FinancialSimulationRequest(
        @NotBlank(message = "국가 코드는 필수입니다.")
        String countryCode,

        @NotNull(message = "월급은 필수입니다.")
        @Positive(message = "월급은 양수여야 합니다.")
        BigDecimal salary,

        String currency,

        @NotNull(message = "고정 지출은 필수입니다.")
        @Positive(message = "고정 지출은 양수여야 합니다.")
        BigDecimal fixedExpense,
        BigDecimal goalAmount
) {}