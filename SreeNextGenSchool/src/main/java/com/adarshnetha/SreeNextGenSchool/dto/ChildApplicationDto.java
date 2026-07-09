package com.adarshnetha.SreeNextGenSchool.dto;

import jakarta.validation.constraints.NotBlank;

public record ChildApplicationDto(
        @NotBlank(message = "Student name is required")
        String studentName,

        @NotBlank(message = "Class applying for is required")
        String classApplyingFor
) {
}