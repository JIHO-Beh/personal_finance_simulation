package com.psc.personal_salary_calculator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

public record ScenarioRequest(
        @NotBlank(message = "국가 코드는 필수입니다.")
        String countryCode,

        List<@NotNull @Positive(message = "고정 지출은 양수여야 합니다.") BigDecimal> fixedExpense
) {}