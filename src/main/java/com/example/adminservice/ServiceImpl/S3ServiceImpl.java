package com.example.adminservice.ServiceImpl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
//import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.adminservice.Service.EntityCrudService;
import com.example.adminservice.Service.S3Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class S3ServiceImpl implements S3Service {
	
    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private EntityCrudService entityCrudService;

    @Value("${s3.bucket}")
    private String bucketName;

    @Value("${s3.folder.path}")
    private String folderPath;
    
    @Override
    public List<String> uploadFiles(List<MultipartFile> files, String category, String refId) throws IOException {
        List<String> fileUrls = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                String fileName = generateFileName(file);
                String folderKey = folderPath + "/" + category + "/" + refId;
                String fileKey = folderKey + "/" + fileName;

                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(file.getContentType());
                metadata.setContentLength(file.getSize());

                amazonS3.putObject(new PutObjectRequest(bucketName, fileKey, file.getInputStream(), metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));

                String fileUrl = "https://amsort.sgp1.digitaloceanspaces.com/" + fileKey;
                fileUrls.add(fileUrl);

                // Create a new file record
                Map<String, Object> apiResponse = new HashMap<>();
                apiResponse.put("storage_path", fileUrl.toString());
                apiResponse.put("filesize", file.getSize());
                apiResponse.put("folder", folderKey);
                apiResponse.put("file_name", fileName);
                apiResponse.put("storage_name", fileKey);

                // Save the file record using the entityCrudService
                entityCrudService.saveEntity("FileRecord", apiResponse);
            }
        } catch (Exception e) {
           
            throw new RuntimeException("Failed to save file record to database"+e);
        }

        return fileUrls;
    }
    @Override
    public void deleteFile(String category, String refId, String fileName) throws IOException {
        String folderKey = folderPath + "/" + category + "/" + refId;
        String fileKey = folderKey + "/" + fileName;
        amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileKey));
    }
    private String generateFileName(MultipartFile file) {
        String fileExtension = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
        return file.getOriginalFilename().toString() ;
    }
}
