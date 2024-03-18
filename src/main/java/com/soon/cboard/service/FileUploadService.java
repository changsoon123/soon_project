package com.soon.cboard.service;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FileUploadService {

    @Autowired
    private AmazonS3 s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public String uploadFile(MultipartFile file) throws IOException {
        // 파일 업로드를 위한 설정
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename(); // 업로드할 파일 이름
        String fileUrl = "https://" + bucketName + ".s3." + s3Client.getRegionName() + ".amazonaws.com/" + fileName; // 업로드된 파일의 URL
        System.out.println(fileUrl);
        // S3에 파일을 업로드합니다.
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), new ObjectMetadata())
                .withCannedAcl(CannedAccessControlList.PublicRead)); // 파일에 대한 공개 읽기 권한 부여

        return fileUrl; // 업로드된 파일의 URL을 반환합니다.
    }
}