package com.soon.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aws")
public class AwsConfig {
    private S3Config s3;

    // Getters and setters
}

class S3Config {
    private String accessKey;
    private String secretKey;
    private String region;

    // Getters and setters
}
