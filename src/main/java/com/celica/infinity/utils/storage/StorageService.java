package com.celica.infinity.utils.storage;

import com.celica.infinity.utils.Utils;
import com.celica.infinity.utils.enums.StorageProviders;
import com.celica.infinity.utils.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class StorageService {

    @Value("${storage.provider}")
    private String PROVIDER;
    private final String joinKey = "--";
    private final AwsStorageService awsService;
    private final LocalStorageService localStorageService;
    private final GoogleStorageService googleStorageService;

    public StorageService(
            AwsStorageService awsService,
            LocalStorageService localStorageService,
            GoogleStorageService googleStorageService
    ){
        this.awsService = awsService;
        this.localStorageService = localStorageService;
        this.googleStorageService = googleStorageService;
    }

    public String getFilename(MultipartFile file) {
        if (file == null) return null;
        String originalFilename = file.getOriginalFilename();
        return PROVIDER + joinKey + UUID.randomUUID().toString() + "_" + originalFilename;
    }

    private String getProvider(String filename) {
        if (Utils.isStringNullOrEmpty(filename)) return null;
        return filename.split(joinKey)[0].toUpperCase();
    }

    public String getFileUrl(String filename) {
        String provider = getProvider(filename);
        if (provider == null) return null;
        if (provider.equalsIgnoreCase(StorageProviders.AWS.toString())){
            return awsService.getPresignedUrl(filename);
        } else if (provider.equalsIgnoreCase(StorageProviders.LOCAL.toString())){
            return localStorageService.getFileUrl(filename);
        } else if (provider.equalsIgnoreCase(StorageProviders.GOOGLE.toString())){
            return googleStorageService.getFileUrl(filename);
        } else {
            return awsService.getPresignedUrl(filename);
        }
    }

    public void saveFile(MultipartFile file, String filename) {
        if (file != null) {
            try {
                if (PROVIDER.equalsIgnoreCase(StorageProviders.LOCAL.toString())) {
                    localStorageService.saveFile(file, filename);
                } else if (PROVIDER.equalsIgnoreCase(StorageProviders.GOOGLE.toString())) {
                    googleStorageService.saveFile(file, filename);
                } else {
                    awsService.saveFile(filename, file.getInputStream(), file.getSize());
                }
            } catch (IOException e) {
                throw new BadRequestException("File upload", e.getMessage());
            }
        }
    }
}
