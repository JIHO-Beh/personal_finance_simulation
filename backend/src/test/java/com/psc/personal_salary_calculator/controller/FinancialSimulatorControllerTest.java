package com.psc.personal_salary_calculator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.psc.personal_salary_calculator.dto.FinancialSimulationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // 스프링 부트 애플리케이션 전체를 테스트 환경에 로드
@AutoConfigureMockMvc // MockMvc(가짜 HTTP 요청 객체)를 자동으로 설정하고 주입
class FinancialSimulatorControllerTest {

    @Autowired // 스프링 컨테이너가 관리하는 Bean을 주입
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Java 객체를 JSON 문자열로 변환하기 위해 사용

    @Test
    @DisplayName("정상적인 요청 시 200 OK 상태와 계산 결과를 반환한다")
    void simulate_Success_WhenRequestIsValid() throws Exception {
        // given (준비)
        FinancialSimulationRequest request = new FinancialSimulationRequest(
                "KR",
                new BigDecimal("3333333"),
                "KRW",
                new BigDecimal("1000000"),
                new BigDecimal("20000000")
        );
        String requestJson = objectMapper.writeValueAsString(request);

        // when (실행)
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/financial-simulate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
        );

        // then (검증)
        resultActions
                .andExpect(status().isOk()) // HTTP 상태가 200 OK 인지 확인
                .andExpect(jsonPath("$.afterTaxSalary").value(2943333)) // JSON 응답의 afterTaxSalary 필드 값 확인
                .andExpect(jsonPath("$.monthsToGoal").value(11)); // JSON 응답의 monthsToGoal 필드 값 확인
    }

    @Test
    @DisplayName("월급에 음수를 입력하는 등 유효하지 않은 요청 시 400 Bad Request를 반환한다")
    void simulate_FailsWithBadRequest_WhenSalaryIsNegative() throws Exception {
        // given (준비)
        FinancialSimulationRequest request = new FinancialSimulationRequest(
                "KR",
                new BigDecimal("-100"), // 유효하지 않은 값
                "KRW",
                new BigDecimal("1000000"),
                null
        );
        String requestJson = objectMapper.writeValueAsString(request);

        // when (실행)
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/financial-simulate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
        );

        // then (검증)
        resultActions
                .andExpect(status().isBadRequest()) // HTTP 상태가 400 Bad Request 인지 확인
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_FAILED")) // 우리가 정의한 에러 코드 확인
                .andExpect(jsonPath("$.message").value("월급은 양수여야 합니다.")); // DTO에 설정한 에러 메시지 확인
    }
}