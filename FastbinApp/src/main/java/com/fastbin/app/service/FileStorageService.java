package com.fastbin.app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fastbin.app.exception.FileStorageException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.storage.path}")
    private String fileStoragePath;

    public String storeFile(String content) {
        try {
            Path filePath = Paths.get(fileStoragePath, UUID.randomUUID().toString() + ".txt");
            Files.write(filePath, content.getBytes());
            return filePath.toString();
        } catch (IOException e) {
            // Handle exception, e.g., log the error and throw a custom exception
            throw new FileStorageException("Failed to store file", e);
        }
    }

    public String readFile(String filePath) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            return new String(bytes);
        } catch (IOException e) {
            // Handle exception, e.g., log the error and throw a custom exception
            throw new FileStorageException("Failed to read file", e);
        }
    }
}