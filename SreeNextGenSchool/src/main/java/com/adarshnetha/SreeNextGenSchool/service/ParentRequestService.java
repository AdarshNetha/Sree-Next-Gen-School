package com.adarshnetha.SreeNextGenSchool.service;

import com.adarshnetha.SreeNextGenSchool.dto.ParentRequestCreateDto;
import com.adarshnetha.SreeNextGenSchool.dto.ParentRequestResponseDto;
import com.adarshnetha.SreeNextGenSchool.dto.ParentRequestUpdateDto;
import com.adarshnetha.SreeNextGenSchool.dto.ResponseStructure;
import com.adarshnetha.SreeNextGenSchool.entity.ParentRequest;
import com.adarshnetha.SreeNextGenSchool.mapper.ParentRequestMapper;
import com.adarshnetha.SreeNextGenSchool.repository.ParentRequestRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParentRequestService {

    private final ParentRequestRepository parentRequestRepository;
    private final ParentRequestMapper parentRequestMapper;

    public ParentRequestService(
            ParentRequestRepository parentRequestRepository,
            ParentRequestMapper parentRequestMapper
    ) {
        this.parentRequestRepository = parentRequestRepository;
        this.parentRequestMapper = parentRequestMapper;
    }

    @Transactional
    public ResponseEntity<ResponseStructure<ParentRequestResponseDto>> createRequest(ParentRequestCreateDto dto) {
        if (parentRequestRepository.existsByPhoneWhatsApp(dto.phoneWhatsApp())) {
            throw new IllegalArgumentException("Request already exists for phone/WhatsApp: " + dto.phoneWhatsApp());
        }
        ParentRequest savedRequest = parentRequestRepository.save(parentRequestMapper.toEntity(dto));
        ParentRequestResponseDto responseDto = parentRequestMapper.toResponseDto(savedRequest);
        ResponseStructure<ParentRequestResponseDto> responseStructure = new ResponseStructure<>(
                "Parent request created successfully",
                responseDto,
                HttpStatus.CREATED.value()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(responseStructure);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ResponseStructure<List<ParentRequestResponseDto>>> getAllRequests() {
        List<ParentRequestResponseDto> responseDtos = parentRequestRepository.findAll()
                .stream()
                .map(parentRequestMapper::toResponseDto)
                .toList();
        ResponseStructure<List<ParentRequestResponseDto>> responseStructure = new ResponseStructure<>(
                "Parent requests fetched successfully",
                responseDtos,
                HttpStatus.OK.value()
        );
        return ResponseEntity.status(HttpStatus.OK).body(responseStructure);
    }

    @Transactional
    public ResponseEntity<ResponseStructure<ParentRequestResponseDto>> updateRequest(Long id, ParentRequestUpdateDto dto) {
        ParentRequest parentRequest = parentRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Request not found with id: " + id));
        parentRequestMapper.updateEntity(parentRequest, dto);
        ParentRequestResponseDto responseDto = parentRequestMapper.toResponseDto(parentRequest);
        ResponseStructure<ParentRequestResponseDto> responseStructure = new ResponseStructure<>(
                "Parent request updated successfully",
                responseDto,
                HttpStatus.OK.value()
        );
        return ResponseEntity.status(HttpStatus.OK).body(responseStructure);
    }
}
