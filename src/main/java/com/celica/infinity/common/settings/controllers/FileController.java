package com.celica.infinity.common.settings.controllers;

import com.celica.infinity.utils.storage.AwsStorageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/files")
public class FileController {

    private final AwsStorageService awsService;

    public FileController(AwsStorageService awsService){
        this.awsService = awsService;
    }

    @GetMapping("/download/{key}")
    public ResponseEntity<byte[]> getFile(@PathVariable String key) {
        byte[] fileBytes = awsService.getFile(key);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + key);
        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
    }
}
