package com.soon.cboard.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
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

    @Value("${aws.s3.access-key}")
    private String accessKey;

    @Value("${aws.s3.secret-key}")
    private String secretKey;

    @Value("${aws.s3.region}")
    private String region;

    public String uploadFile(MultipartFile file) throws IOException {
        // AWS S3 클라이언트 설정
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();

        // 파일 업로드를 위한 설정
        String bucketName = "soonproject"; // 업로드할 버킷 이름
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename(); // 업로드할 파일 이름
        String fileUrl = "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName; // 업로드된 파일의 URL

        // S3에 파일을 업로드합니다.
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), new ObjectMetadata())
                .withCannedAcl(CannedAccessControlList.PublicRead)); // 파일에 대한 공개 읽기 권한 부여

        return fileUrl; // 업로드된 파일의 URL을 반환합니다.
    }
}