package com.psc.personal_salary_calculator.service;

import com.psc.personal_salary_calculator.domain.Country;
import com.psc.personal_salary_calculator.domain.TaxRate;
import com.psc.personal_salary_calculator.dto.FinancialSimulationRequest;
import com.psc.personal_salary_calculator.dto.FinancialSimulationResponse;
import com.psc.personal_salary_calculator.repository.CountryRepository;
import com.psc.personal_salary_calculator.repository.TaxRateRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows; // assertThrows import
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
    @DisplayName("정상적인 요청 시 세후 월급과 목표 달성 개월 수를 정확히 계산한다")
    void calculate_Success_WhenRequestIsValid() {
        // given (준비)
        FinancialSimulationRequest request = new FinancialSimulationRequest(
                "KR",
                new BigDecimal("3333333"),
                "KRW",
                new BigDecimal("1000000"),
                new BigDecimal("20000000")
        );
        Country mockCountry = mock(Country.class);
        TaxRate mockTaxRate = mock(TaxRate.class);
        when(mockTaxRate.getIncomeTaxRate()).thenReturn(new BigDecimal("0.0230"));
        when(mockTaxRate.getSocialSecurityRate()).thenReturn(new BigDecimal("0.0940"));
        when(countryRepository.findByCountryCode("KR")).thenReturn(Optional.of(mockCountry));
        when(taxRateRepository.findByCountry(any(Country.class))).thenReturn(Optional.of(mockTaxRate));

        // when (실행)
        FinancialSimulationResponse response = simulationService.calculate(request);

        // then (검증)
        assertThat(response).isNotNull();
        assertThat(response.afterTaxSalary().intValue()).isEqualTo(2943333);
        assertThat(response.monthsToGoal()).isEqualTo(11);
    }

    // --- 여기부터 새로운 테스트 메소드 ---
    @Test
    @DisplayName("지원하지 않는 국가 코드로 요청 시 IllegalArgumentException을 던진다")
    void calculate_ThrowsIllegalArgumentException_WhenCountryCodeIsInvalid() {
        // given (준비)
        String invalidCountryCode = "XX";
        FinancialSimulationRequest request = new FinancialSimulationRequest(
                invalidCountryCode,
                new BigDecimal("5000"),
                "USD",
                new BigDecimal("1000"),
                new BigDecimal("50000")
        );
        // DB에 해당 국가 코드가 없음을 시뮬레이션
        when(countryRepository.findByCountryCode(invalidCountryCode)).thenReturn(Optional.empty());

        // when & then (실행 및 검증)
        // simulationService.calculate(request)를 실행했을 때,
        // IllegalArgumentException이 발생하는지 검증합니다.
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            simulationService.calculate(request);
        });

        // 발생한 예외의 메시지가 기대와 일치하는지 추가로 검증합니다.
        assertThat(exception.getMessage()).isEqualTo("지원하지 않는 국가 코드입니다: " + invalidCountryCode);
    }
}