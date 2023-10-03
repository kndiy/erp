package com.kndiy.erp.others;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TreeMap;

public class DownloadFileFromAwsS3ByTime {

    final private AmazonS3 s3Client;
    private String bucketName;
    private final TreeMap<String, String> s3ObjectKeyTreeMap = new TreeMap<>();

    public DownloadFileFromAwsS3ByTime(String accessKey, String secretKey) {

        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

        s3Client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.AP_SOUTHEAST_1)
                .build();
    }

    public void parseAllS3ObjectKeysIntoTreeMap(String bucketName, String folderName) {

        this.bucketName = bucketName;

        ObjectListing listing = s3Client.listObjects(bucketName, folderName);

        List<S3ObjectSummary> summaryList = listing.getObjectSummaries();

        while (listing.isTruncated()) {
            listing = s3Client.listNextBatchOfObjects(listing);
            summaryList.addAll(listing.getObjectSummaries());
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime localDateTime;

        for (S3ObjectSummary summary : summaryList) {

            localDateTime = summary.getLastModified().toInstant().atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime();

            String time = dtf.format(localDateTime);

            s3ObjectKeyTreeMap.put(time, summary.getKey());
        }
    }

    public byte[] outputByteArray(FILE_LOCATION fileLocation) {

        String s3Key = null;

        switch (fileLocation) {
            case FIRST -> s3Key = s3ObjectKeyTreeMap.firstEntry().getValue();
            case LAST -> s3Key = s3ObjectKeyTreeMap.lastEntry().getValue();
        }

        return makeByteArrayWithS3Key(s3Key);
    }

    public byte[] outputByteArray(String yyyyMMddHHmmss) {

        String s3Key = s3ObjectKeyTreeMap.get(yyyyMMddHHmmss);

        return makeByteArrayWithS3Key(s3Key);
    }

    private byte[] makeByteArrayWithS3Key(String s3Key) {

        S3Object file = s3Client.getObject(new GetObjectRequest(bucketName, s3Key));
        try {
            InputStream inputStream = file.getObjectContent();
            return inputStream.readAllBytes();
        }
        catch (IOException ioException) {
            throw new IllegalArgumentException("No data to read");
        }
    }

    public static enum FILE_LOCATION {
        FIRST, LAST
    }

}
