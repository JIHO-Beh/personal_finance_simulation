package com.psc.personal_salary_calculator.dto;

// API 에러 발생 시 응답으로 사용할 DTO
public record ErrorResponse(
        String errorCode,
        String message
) {}
