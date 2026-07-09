package com.adarshnetha.SreeNextGenSchool.mapper;

import com.adarshnetha.SreeNextGenSchool.dto.ChildApplicationDto;
import com.adarshnetha.SreeNextGenSchool.dto.ParentRequestCreateDto;
import com.adarshnetha.SreeNextGenSchool.dto.ParentRequestResponseDto;
import com.adarshnetha.SreeNextGenSchool.dto.ParentRequestUpdateDto;
import com.adarshnetha.SreeNextGenSchool.entity.ParentRequest;
import com.adarshnetha.SreeNextGenSchool.entity.RequestStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ParentRequestMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ParentRequest toEntity(ParentRequestCreateDto dto) {
        ParentRequest parentRequest = new ParentRequest();
        parentRequest.setStudentName(dto.studentName());
        parentRequest.setParentName(dto.parentName());
        parentRequest.setPhoneWhatsApp(dto.phoneWhatsApp());
        parentRequest.setClassApplyingFor(dto.classApplyingFor());
        parentRequest.setMessage(dto.message());
        parentRequest.setAdditionalChildrenJson(writeChildrenJson(dto.additionalChildren()));
        parentRequest.setStatus(RequestStatus.REQUESTED_FOR_ADMISSION);
        return parentRequest;
    }

    public ParentRequestResponseDto toResponseDto(ParentRequest parentRequest) {
        return new ParentRequestResponseDto(
                parentRequest.getId(),
            parentRequest.getStudentName(),
                parentRequest.getParentName(),
            parentRequest.getPhoneWhatsApp(),
            parentRequest.getClassApplyingFor(),
            parentRequest.getMessage(),
            readChildrenJson(parentRequest.getAdditionalChildrenJson()),
                parentRequest.getStatus(),
                parentRequest.getTimestamp()
        );
    }

    public void updateEntity(ParentRequest parentRequest, ParentRequestUpdateDto dto) {
        parentRequest.setStudentName(dto.studentName());
        parentRequest.setParentName(dto.parentName());
        parentRequest.setPhoneWhatsApp(dto.phoneWhatsApp());
        parentRequest.setClassApplyingFor(dto.classApplyingFor());
        parentRequest.setMessage(dto.message());
        parentRequest.setAdditionalChildrenJson(writeChildrenJson(dto.additionalChildren()));
        parentRequest.setStatus(dto.status());
    }

    private String writeChildrenJson(List<ChildApplicationDto> children) {
        if (children == null || children.isEmpty()) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(children);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Unable to serialize additional children", exception);
        }
    }

    private List<ChildApplicationDto> readChildrenJson(String childrenJson) {
        if (childrenJson == null || childrenJson.isBlank()) {
            return List.of();
        }

        try {
            return objectMapper.readValue(childrenJson, new TypeReference<List<ChildApplicationDto>>() {
            });
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Unable to deserialize additional children", exception);
        }
    }
}
