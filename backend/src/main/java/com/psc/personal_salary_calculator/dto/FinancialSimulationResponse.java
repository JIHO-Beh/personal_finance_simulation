package com.psc.personal_salary_calculator.dto;

import java.math.BigDecimal;

public record FinancialSimulationResponse(
        BigDecimal afterTaxSalary,
        Integer monthsToGoal
) {}