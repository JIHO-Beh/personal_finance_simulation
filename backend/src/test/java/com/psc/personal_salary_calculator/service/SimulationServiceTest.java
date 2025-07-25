package com.psc.personal_salary_calculator.service;

import com.psc.personal_salary_calculator.domain.Country;
import com.psc.personal_salary_calculator.domain.TaxRate;
import com.psc.personal_salary_calculator.dto.FinancialSimulationRequest;
import com.psc.personal_salary_calculator.dto.ScenarioRequest;
import com.psc.personal_salary_calculator.dto.ScenarioResult;
import com.psc.personal_salary_calculator.repository.CountryRepository;
import com.psc.personal_salary_calculator.repository.TaxRateRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimulationServiceTest {

    @InjectMocks
    private SimulationService simulationService;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private TaxRateRepository taxRateRepository;

    @Test
    @DisplayName("시나리오 요청 시 정확히 계산한다")
    void calculate_Success_WhenScenarioListIsValid() {
        // given
        ScenarioRequest scenarioRequest = new ScenarioRequest("KR", List.of(new BigDecimal("1000000")));
        FinancialSimulationRequest request = new FinancialSimulationRequest(
                new BigDecimal("3333333"),
                new BigDecimal("20000000"),
                List.of(scenarioRequest)
        );

        Country mockCountry = mock(Country.class);
        TaxRate mockTaxRate = mock(TaxRate.class);
        when(mockTaxRate.getIncomeTaxRate()).thenReturn(new BigDecimal("0.0230"));
        when(mockTaxRate.getSocialSecurityRate()).thenReturn(new BigDecimal("0.0940"));
        when(countryRepository.findByCountryCode("KR")).thenReturn(Optional.of(mockCountry));
        when(taxRateRepository.findByCountry(any(Country.class))).thenReturn(Optional.of(mockTaxRate));

        // when
        List<ScenarioResult> results = simulationService.calculate(request);

        // then
        assertThat(results).hasSize(1);
        ScenarioResult result = results.get(0);
        assertThat(result.afterTaxSalary().intValue()).isEqualTo(2943333);
        assertThat(result.monthsToGoal()).isEqualTo(11);
    }

    @Test
    @DisplayName("시나리오 목록과 국가 코드가 없을 때 한국(KR) 기준으로 기본 계산한다")
    void calculate_Success_DefaultsToKorea_WhenCountryCodeIsMissing() {
        // given
        FinancialSimulationRequest request = new FinancialSimulationRequest(
                new BigDecimal("4000000"),
                null, // 목표 금액 없음 (기본값 적용)
                null  // 시나리오 목록 없음
        );

        Country mockCountry = mock(Country.class);
        TaxRate mockTaxRate = mock(TaxRate.class);
        when(mockTaxRate.getIncomeTaxRate()).thenReturn(new BigDecimal("0.0230"));
        when(mockTaxRate.getSocialSecurityRate()).thenReturn(new BigDecimal("0.0940"));
        // 서비스 로직이 "KR"을 사용할 것이므로, "KR"에 대한 Mock 행동을 정의
        when(countryRepository.findByCountryCode("KR")).thenReturn(Optional.of(mockCountry));
        when(taxRateRepository.findByCountry(any(Country.class))).thenReturn(Optional.of(mockTaxRate));

        // when
        List<ScenarioResult> results = simulationService.calculate(request);

        // then
        assertThat(results).hasSize(1);
        ScenarioResult result = results.get(0);
        // 기본값인 KR로 계산되었는지 확인
        assertThat(result.countryCode()).isEqualTo("KR");
        assertThat(result.totalFixedExpense()).isEqualTo(BigDecimal.ZERO); // 고정지출 0원 확인
        assertThat(result.afterTaxSalary().intValue()).isEqualTo(3532000); // 400만 * (1-0.117)
        assertThat(result.monthsToGoal()).isEqualTo(14); // 4800만 / 3532000 = 13.58... -> 14
    }
}