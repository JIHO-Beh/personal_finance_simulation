package com.psc.personal_salary_calculator.service;

import com.psc.personal_salary_calculator.domain.Country;
import com.psc.personal_salary_calculator.domain.TaxRate;
import com.psc.personal_salary_calculator.dto.*;
import com.psc.personal_salary_calculator.repository.CountryRepository;
import com.psc.personal_salary_calculator.repository.TaxRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SimulationService {

    //국가리포지토리
    private final CountryRepository countryRepository;
    //세금리포지토리
    private final TaxRateRepository taxRateRepository;

    //국가 리스트 습득
    @Transactional(readOnly = true)
    public List<SupportedCountryResponse> getSupportedCountries() {
        return countryRepository.findAll().stream()
                .map(SupportedCountryResponse::from)
                .collect(Collectors.toList());
    }

    //계산 로직 시작
    @Transactional(readOnly = true)
    public List<ScenarioResult> calculate(FinancialSimulationRequest request) {
        // 'scenarios' 필드가 있는지 확인
        boolean hasScenarios = request.scenarios() != null && !request.scenarios().isEmpty();

        if (hasScenarios) {
            // 여러 시나리오가 있는 경우: 각 시나리오를 반복 처리
            return request.scenarios().stream()
                    .map(scenario -> processScenario(request.salary(), request.goalAmount(), scenario))
                    .collect(Collectors.toList());
        } else {
            // 시나리오가 없는 경우: '한국', '고정 지출 0'을 기본값으로 단일 계산
            ScenarioRequest defaultScenario = new ScenarioRequest("KR", Collections.emptyList());
            ScenarioResult result = processScenario(request.salary(), request.goalAmount(), defaultScenario);

            // 단일 결과를 리스트에 담아 반환
            return Collections.singletonList(result);
        }
    }

    //단일 시나리오에 대한 계산을 처리하는 private 메소드
    private ScenarioResult processScenario(BigDecimal salary, BigDecimal goalAmount, ScenarioRequest scenario) {
        //리퀘스트의 국가 코드 조회
        Country country = countryRepository.findByCountryCode(scenario.countryCode())
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 국가 코드입니다: " + scenario.countryCode()));

        //리퀘스트의 국가 세율 조회
        TaxRate taxRate = taxRateRepository.findByCountry(country)
                .orElseThrow(() -> new IllegalStateException("해당 국가의 세율 정보가 없습니다: " + country.getCountryName()));

        //총 세율(소득세율 + 사회보장세율)
        BigDecimal totalTaxRate = taxRate.getIncomeTaxRate().add(taxRate.getSocialSecurityRate());
        //총 세금(월급 + 총 세율)
        BigDecimal taxAmount = salary.multiply(totalTaxRate);
        //세후 월급(월급 - 총 세금)
        BigDecimal afterTaxSalary = salary.subtract(taxAmount);

        //고정 지출 합산 (null 또는 비어있으면 0으로 처리)
        List<BigDecimal> expenses = scenario.fixedExpense();
        BigDecimal totalFixedExpense = (expenses == null || expenses.isEmpty())
                ? BigDecimal.ZERO
                : expenses.stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        //월 저축 가능액(세후 월급 - 고정지출)
        BigDecimal netMonthlySaving = afterTaxSalary.subtract(totalFixedExpense);

        //목표금액: 리퀘스트의 목표금액이 null이 아니거나 0이 아닌 양수 일때, 리퀘스트의 목표금액 사용.
        //         그렇지 않은 경우 12개월 치 월급을 목표금액으로 설정.
        BigDecimal finalGoalAmount = (goalAmount != null && goalAmount.compareTo(BigDecimal.ZERO) > 0)
                ? goalAmount
                : salary.multiply(BigDecimal.valueOf(12));

        //달성 개월 수 계산
        Integer monthsToGoal;
        //월 저축 가능액이 0이거나 음수일때, -1을 반환한다.
        if (netMonthlySaving.compareTo(BigDecimal.ZERO) <= 0) {
            monthsToGoal = -1;
        } else {
            //달성 개월 수(목표금액 / 월 저축 가능액) 소수점은 올림 처림
            monthsToGoal = finalGoalAmount.divide(netMonthlySaving, 0, RoundingMode.CEILING).intValue();
        }

        /*
            단일 시나리오의 계산 결과를 반환한다.
            (
                국가 코드,
                고정 지출 리스트,
                고정 지출 합계,
                세후 월급,
                월 저축 가능액,
                달성 개월 수
            )
         */
        return new ScenarioResult(
                scenario.countryCode(),
                expenses,
                totalFixedExpense,
                afterTaxSalary,
                netMonthlySaving,
                monthsToGoal
        );
    }
}