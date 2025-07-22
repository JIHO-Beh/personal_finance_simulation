package com.psc.personal_salary_calculator.dto;

import java.math.BigDecimal;
import java.util.List;

public record ScenarioResult(
        String countryCode,
        List<BigDecimal> fixedExpense,
        BigDecimal totalFixedExpense,
        BigDecimal afterTaxSalary,
        BigDecimal netMonthlySaving,
        Integer monthsToGoal
) {}