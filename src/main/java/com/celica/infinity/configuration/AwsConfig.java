package com.celica.infinity.configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudfront.AmazonCloudFront;
import com.amazonaws.services.cloudfront.AmazonCloudFrontClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

    @Value("${aws.keys.access-key}")
    private String ACCESS_KEY;

    @Value("${aws.keys.access-secret}")
    private String ACCESS_SECRET;

    @Value("${aws.s3.region}")
    private String REGION;

    @Bean
    public AmazonS3 getAmazonS3Client() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY,ACCESS_SECRET);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(REGION)
                .build();
    }

    @Bean
    public AmazonCloudFront getAmazonCloudFront() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY,ACCESS_SECRET);
        return AmazonCloudFrontClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(REGION)
                .build();
    }
}
