package com.psc.personal_salary_calculator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.psc.personal_salary_calculator.dto.FinancialSimulationRequest;
import com.psc.personal_salary_calculator.dto.ScenarioRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // 스프링 부트 애플리케이션 전체를 테스트 환경에 로드
@AutoConfigureMockMvc // MockMvc(가짜 HTTP 요청 객체)를 자동으로 설정하고 주입
class FinancialSimulatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // [기존 테스트 수정] 여러 시나리오 요청 테스트
    @Test
    @DisplayName("여러 시나리오를 포함한 정상적인 요청 시 200 OK와 결과 배열을 반환한다")
    void simulate_Success_ForMultipleScenarios() throws Exception {
        // given
        ScenarioRequest usScenario = new ScenarioRequest("US", List.of(new BigDecimal("1500")));
        ScenarioRequest krScenario = new ScenarioRequest("KR", List.of(new BigDecimal("1200")));
        FinancialSimulationRequest request = new FinancialSimulationRequest(
                new BigDecimal("5000"),
                new BigDecimal("60000"),
                null, null,
                List.of(usScenario, krScenario)
        );
        String requestJson = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/financial-simulation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray()) // 응답이 배열인지 확인
                .andExpect(jsonPath("$.length()").value(2)) // 배열의 크기가 2인지 확인
                .andExpect(jsonPath("$[0].countryCode").value("US"))
                .andExpect(jsonPath("$[1].countryCode").value("KR"));
    }

    // [신규 테스트] 기본값(한국) 계산 테스트
    @Test
    @DisplayName("시나리오와 국가코드 없이 요청 시 한국 기준으로 기본 계산 후 200 OK를 반환한다")
    void simulate_Success_ForDefaultKoreaCase() throws Exception {
        // given
        FinancialSimulationRequest request = new FinancialSimulationRequest(
                new BigDecimal("4000000"),
                null,
                null, // 국가 코드 없음
                List.of(new BigDecimal("1000000")),
                null  // 시나리오 없음
        );
        String requestJson = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/financial-simulation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].countryCode").value("KR"))
                .andExpect(jsonPath("$[0].monthsToGoal").value(19));
    }

    // [기존 테스트 수정] 유효성 검증 실패 테스트
    @Test
    @DisplayName("월급에 음수를 입력하는 등 유효하지 않은 요청 시 400 Bad Request를 반환한다")
    void simulate_FailsWithBadRequest_WhenSalaryIsNegative() throws Exception {
        // given
        FinancialSimulationRequest request = new FinancialSimulationRequest(
                new BigDecimal("-100"), // 유효하지 않은 값
                null,
                "KR",
                List.of(new BigDecimal("1000000")),
                null
        );
        String requestJson = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/financial-simulation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
        );

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_FAILED"))
                .andExpect(jsonPath("$.message").value("월급은 양수여야 합니다."));
    }
}