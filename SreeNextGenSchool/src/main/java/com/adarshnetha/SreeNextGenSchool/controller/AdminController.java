package com.adarshnetha.SreeNextGenSchool.controller;

import com.adarshnetha.SreeNextGenSchool.dto.ParentRequestUpdateDto;
import com.adarshnetha.SreeNextGenSchool.dto.ParentRequestResponseDto;
import com.adarshnetha.SreeNextGenSchool.dto.ResponseStructure;
import com.adarshnetha.SreeNextGenSchool.service.ParentRequestService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ParentRequestService parentRequestService;

    public AdminController(ParentRequestService parentRequestService) {
        this.parentRequestService = parentRequestService;
    }

    @GetMapping("/requests")
    public ResponseEntity<ResponseStructure<List<ParentRequestResponseDto>>> getParentRequests() {
        return parentRequestService.getAllRequests();
    }

    @PutMapping("/requests/{id}")
    public ResponseEntity<ResponseStructure<ParentRequestResponseDto>> updateParentRequest(
            @PathVariable Long id,
            @Valid @RequestBody ParentRequestUpdateDto dto
    ) {
        return parentRequestService.updateRequest(id, dto);
    }
}
