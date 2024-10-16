package com.celica.infinity.common.settings.controllers;

import com.celica.infinity.common.settings.dtos.requests.CreateKycRequestDto;
import com.celica.infinity.common.settings.dtos.responses.KycDto;
import com.celica.infinity.common.settings.services.KycService;
import com.celica.infinity.utils.annotations.pagination.dtos.PaginatedResponseDto;
import com.celica.infinity.utils.annotations.sorting.RequestSorting;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/settings/kyc")
public class KycController {

    private final KycService kycService;

    public KycController(KycService kycService) {
        this.kycService = kycService;
    }

    @PostMapping()
    public ResponseEntity<KycDto> configureKyc(@RequestBody @Valid CreateKycRequestDto kycRequestDto) {
        var kyc = kycService.configureKyc(kycRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(kyc);
    }

    @GetMapping()
    public ResponseEntity<PaginatedResponseDto<KycDto>> getKycs(
            @RequestSorting PageRequest pageRequest
    ) {
        var kycs = kycService.getKycs(pageRequest);
        return ResponseEntity.status(HttpStatus.OK).body(kycs);
    }

    @GetMapping("{id}")
    public ResponseEntity<KycDto> getKyc(@PathVariable Long id) {
        var kys = kycService.getKyc(id);
        return ResponseEntity.status(HttpStatus.OK).body(kys);
    }
}
