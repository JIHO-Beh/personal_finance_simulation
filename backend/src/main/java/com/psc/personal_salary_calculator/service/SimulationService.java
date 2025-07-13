package com.psc.personal_salary_calculator.service;

import com.psc.personal_salary_calculator.domain.Country;
import com.psc.personal_salary_calculator.domain.TaxRate;
import com.psc.personal_salary_calculator.dto.FinancialSimulationRequest;
import com.psc.personal_salary_calculator.dto.FinancialSimulationResponse;
import com.psc.personal_salary_calculator.dto.SupportedCountryResponse;
import com.psc.personal_salary_calculator.repository.CountryRepository;
import com.psc.personal_salary_calculator.repository.TaxRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    //계산
    @Transactional(readOnly = true)
    public FinancialSimulationResponse calculate(FinancialSimulationRequest request) {
        //리퀘스트의 국가 코드 조회
        Country country = countryRepository.findByCountryCode(request.countryCode())
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 국가 코드입니다: " + request.countryCode()));
        //리퀘스트의 국가 세율 조회
        TaxRate taxRate = taxRateRepository.findByCountry(country)
                .orElseThrow(() -> new IllegalStateException("해당 국가의 세율 정보가 없습니다: " + country.getCountryName()));

        //리퀘스트의 월급
        BigDecimal salary = request.salary();
        //총 세율(소득세율 + 사회보장세율)
        BigDecimal totalTaxRate = taxRate.getIncomeTaxRate().add(taxRate.getSocialSecurityRate());
        //총 세금(월급 + 총 세율)
        BigDecimal taxAmount = salary.multiply(totalTaxRate);
        //세후 월급(월급 - 총 세금)
        BigDecimal afterTaxSalary = salary.subtract(taxAmount);

        //목표금액: 리퀘스트의 목표금액이 null이 아니거나 0이 아닌 양수 일때, 리퀘스트의 목표금액 사용.
        //         그렇지 않은 경우 12개월 치 월급을 목표금액으로 설정.
        BigDecimal goalAmount = (request.goalAmount() != null && request.goalAmount().compareTo(BigDecimal.ZERO) > 0)
                ? request.goalAmount()
                : salary.multiply(BigDecimal.valueOf(12));

        //월 저축액 가능액(세후 월급 - 고정지출)
        BigDecimal netMonthlySaving = afterTaxSalary.subtract(request.fixedExpense());

        //월 저축액 가능액이 0이거나 음수일때, -1을 반환한다.
        if (netMonthlySaving.compareTo(BigDecimal.ZERO) <= 0) {
            return new FinancialSimulationResponse(afterTaxSalary, -1);
        }

        //월 저축액(목표금액 / 월 저축액 가능액) 소수점은 올림 처림
        BigDecimal monthsDecimal = goalAmount.divide(netMonthlySaving, 0, RoundingMode.CEILING);
        //달성 개월 수
        Integer monthsToGoal = monthsDecimal.intValue();

        //세후 금액과 달성 개월 수를 반환한다.
        return new FinancialSimulationResponse(afterTaxSalary, monthsToGoal);
    }
}