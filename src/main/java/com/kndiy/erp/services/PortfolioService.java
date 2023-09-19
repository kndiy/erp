package com.kndiy.erp.services;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TreeMap;

@Service
@Slf4j
@PropertySource(ignoreResourceNotFound = true, value = "classpath:application.yml")
public class PortfolioService {

    @Value("${ak}")
    private String ak;
    @Value("${sk}")
    private String sk;
    public void serveResume(OutputStream outputStream) throws IOException {

        AmazonS3 amazonS3Client = getAmazonS3Client();

        String bucketName = "230919-kndiy-file-bucket";
        String folderName = "cv/";

        TreeMap<String, String> s3ObjectKeysTreeMap = makeS3objectKeysTreeMap(amazonS3Client, bucketName, folderName);

        String key = s3ObjectKeysTreeMap.lastEntry().getValue();
        S3Object lastFile = amazonS3Client.getObject(new GetObjectRequest(bucketName, key));

        byte[] bytes = makeByteArrayFromS3Object(lastFile);

        for (int i = 0; i < bytes.length; i ++) {
            outputStream.write(bytes[i]);
        }
    }

    private AmazonS3 getAmazonS3Client() {

        AWSCredentials awsCredentials = new BasicAWSCredentials(ak, sk);

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.AP_SOUTHEAST_1)
                .build();
    }

    private TreeMap<String, String> makeS3objectKeysTreeMap(AmazonS3 s3Client, String bucketName, String folderName) {

        TreeMap<String, String> s3ObjectKeysTreeMap = new TreeMap<>();

        ObjectListing listing = s3Client.listObjects(bucketName, folderName);

        List<S3ObjectSummary> summaryList = listing.getObjectSummaries();

        while (listing.isTruncated()) {
            listing = s3Client.listNextBatchOfObjects(listing);
            summaryList.addAll(listing.getObjectSummaries());
        }

        for (S3ObjectSummary summary : summaryList) {

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime localDateTime = summary.getLastModified().toInstant().atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime();

            String time = dtf.format(localDateTime);

            s3ObjectKeysTreeMap.put(time, summary.getKey());
        }

        return s3ObjectKeysTreeMap;
    }

    private byte[] makeByteArrayFromS3Object(S3Object lastFile) {

        try {
            InputStream inputStream = lastFile.getObjectContent();
            return inputStream.readAllBytes();
        }
        catch (IOException ioException) {
            throw new IllegalArgumentException("No data to read");
        }
    }
}
