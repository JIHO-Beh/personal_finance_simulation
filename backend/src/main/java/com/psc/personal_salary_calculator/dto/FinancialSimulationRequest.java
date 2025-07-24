package com.psc.personal_salary_calculator.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

public record FinancialSimulationRequest(
        @NotNull(message = "월급은 필수입니다.")
        @Positive(message = "월급은 양수여야 합니다.")
        BigDecimal salary,

        BigDecimal goalAmount,

        List<@Valid ScenarioRequest> scenarios
) {}
