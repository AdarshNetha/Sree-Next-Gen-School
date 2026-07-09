package com.adarshnetha.SreeNextGenSchool.controller;

import com.adarshnetha.SreeNextGenSchool.dto.ParentRequestCreateDto;
import com.adarshnetha.SreeNextGenSchool.dto.ParentRequestResponseDto;
import com.adarshnetha.SreeNextGenSchool.dto.ResponseStructure;
import com.adarshnetha.SreeNextGenSchool.service.ParentRequestService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/requests", "/enquiries"})
public class ParentRequestController {

    private final ParentRequestService parentRequestService;

    public ParentRequestController(ParentRequestService parentRequestService) {
        this.parentRequestService = parentRequestService;
    }

    @PostMapping
    public ResponseEntity<ResponseStructure<ParentRequestResponseDto>> createRequest(
            @Valid @RequestBody ParentRequestCreateDto dto
    ) {
        return parentRequestService.createRequest(dto);
    }
}
