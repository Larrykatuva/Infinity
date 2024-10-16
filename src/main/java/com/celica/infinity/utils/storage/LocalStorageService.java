package com.celica.infinity.utils.storage;

import com.celica.infinity.utils.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Service
public class LocalStorageService {
    @Value("${storage.local.path}")
    private String uploadDir;

    public void saveFile(MultipartFile file, String filename) {
        File directory = new File(uploadDir);
        if (!directory.exists()) directory.mkdirs();
        try {
            file.transferTo(new File(uploadDir, filename));
        } catch (IOException e) {
            throw new BadRequestException("Local file storage", e.getMessage());
        }
    }

    public String getFileUrl(String filename) {
        return Paths.get(uploadDir).resolve(filename).toString();
    }
}
