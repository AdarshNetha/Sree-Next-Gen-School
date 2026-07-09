package com.adarshnetha.SreeNextGenSchool.dto;

import com.adarshnetha.SreeNextGenSchool.entity.RequestStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;

public record ParentRequestUpdateDto(
        @NotBlank(message = "Student name is required")
        String studentName,

        @NotBlank(message = "Parent name is required")
        String parentName,

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^[6-9]\\d{9}$", message = "Mobile number must be a valid 10 digit Indian phone number")
        String phoneWhatsApp,

        @NotBlank(message = "Class applying for is required")
        String classApplyingFor,

        String message,

        List<ChildApplicationDto> additionalChildren,

        @NotNull(message = "Status is required")
        RequestStatus status
) {
}
