package com.celica.infinity.utils.storage;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.celica.infinity.utils.Utils;
import com.celica.infinity.utils.notification.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AwsStorageService {

    @Value("${aws.cloud-front.private-key}")
    private String PRIVATE_KEY;

    @Value("${aws.cloud-front.enabled}")
    private boolean CLOUD_FRONT_ENABLED;

    @Value("${aws.cloud-front.domain}")
    private String DISTRIBUTION_DOMAIN;

    @Value("${aws.cloud-front.pair-key-id}")
    private String PAIR_KEY_ID;

    @Value("${aws.s3.bucket-name}")
    private String BUCKET_NAME;

    @Value("${aws.s3.expiry-milliseconds}")
    private Long URL_EXPIRY;

    private final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final AmazonS3 s3Client;

    public AwsStorageService(AmazonS3 amazonS3) {
        this.s3Client = amazonS3;
    }

    public void saveFile(String objectName, InputStream inputStream, long contentLength) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            s3Client.putObject(BUCKET_NAME, objectName, inputStream, metadata);
        } catch (AmazonServiceException e) {
            logger.error("Error occurred while saving to aws bucket "+e);
        }
    }

    private Date generateExpiryDate() {
        Date urlExpiry = new Date();
        long expiryTimeMillis = urlExpiry.getTime();
        expiryTimeMillis += URL_EXPIRY;
        urlExpiry.setTime(expiryTimeMillis);
        return urlExpiry;
    }

    public byte[] getFile(String key) {
        S3Object s3Object = s3Client.getObject(BUCKET_NAME, key);
        try (InputStream inputStream = s3Object.getObjectContent();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            logger.error("Error occurred while retrieving file from aws bucket"+e);
        }
        return null;
    }

    public String getPresignedUrl(String key) {
        if (Utils.isStringNullOrEmpty(key)) return null;
        if (CLOUD_FRONT_ENABLED)
            return generatePresignedCachedUrl(key);
        return generatePresignedUrl(key);
    }

    public String generatePresignedUrl(String key) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(BUCKET_NAME, key)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(generateExpiryDate());
        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    public String generatePresignedCachedUrl(String key) {
        String resourceUrl = String.format("https://%s/%s", DISTRIBUTION_DOMAIN, key);
//        return CloudFrontUrlSigner.getSignedURLWithCannedPolicy(
//                resourceUrl,
//                PAIR_KEY_ID,
//                new PrivateKey(),
//                generateExpiryDate()
//        );
        return resourceUrl;
    }
}
