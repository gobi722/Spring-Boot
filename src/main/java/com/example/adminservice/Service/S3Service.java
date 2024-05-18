package com.example.adminservice.Service;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface S3Service {
    List<String> uploadFiles(List<MultipartFile> files, String category, String refId) throws IOException;
}

