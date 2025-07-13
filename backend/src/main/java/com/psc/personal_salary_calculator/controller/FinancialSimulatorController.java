package com.psc.personal_salary_calculator.controller;

import com.psc.personal_salary_calculator.dto.FinancialSimulationRequest;
import com.psc.personal_salary_calculator.dto.FinancialSimulationResponse;
import com.psc.personal_salary_calculator.dto.SupportedCountryResponse;
import com.psc.personal_salary_calculator.service.SimulationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FinancialSimulatorController {

    private final SimulationService simulationService;

    @GetMapping("/supported-countries")
    public List<SupportedCountryResponse> getSupportedCountries() {
        return simulationService.getSupportedCountries();
    }

    @PostMapping("/financial-simulate")
    public FinancialSimulationResponse simulate(@Valid @RequestBody FinancialSimulationRequest request) {
                return simulationService.calculate(request);
    }
}