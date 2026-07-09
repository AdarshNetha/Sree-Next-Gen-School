package com.adarshnetha.SreeNextGenSchool.dto;

import com.adarshnetha.SreeNextGenSchool.entity.RequestStatus;
import java.time.LocalDateTime;
import java.util.List;

public record ParentRequestResponseDto(
        Long id,
        String studentName,
        String parentName,
        String phoneWhatsApp,
        String classApplyingFor,
        String message,
        List<ChildApplicationDto> additionalChildren,
        RequestStatus status,
        LocalDateTime timestamp
) {
}
