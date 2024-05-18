package com.example.adminservice.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.adminservice.Service.S3Service;
import com.example.adminservice.ServiceImpl.ResponseService;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Autowired
    private S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") List<MultipartFile> file, @RequestParam("category") String category, @RequestParam("refId") String refId) throws IOException {
        try {     
            List<String> fileUrl = s3Service.uploadFiles(file, category, refId);;
            return ResponseService.successResponse(fileUrl);
        } catch (Exception e) {
            return ResponseService.badRequest(e.getMessage());
        }
    }
}
