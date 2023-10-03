package com.kndiy.erp.services;

import com.kndiy.erp.others.DownloadFileFromAwsS3ByTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@Slf4j
@PropertySource(ignoreResourceNotFound = true, value = "classpath:application.yml")
public class PortfolioService {

    @Value("${ak}")
    private String ak;
    @Value("${sk}")
    private String sk;
    public void serveResume(OutputStream outputStream) throws IOException {

        String bucketName = "elasticbeanstalk-ap-southeast-1-768940076020";
        String folderName = "cv/";

        DownloadFileFromAwsS3ByTime downloadFileFromAwsS3ByTime = new DownloadFileFromAwsS3ByTime(ak, sk);
        downloadFileFromAwsS3ByTime.parseAllS3ObjectKeysIntoTreeMap(bucketName, folderName);

        byte[] bytes = downloadFileFromAwsS3ByTime.outputByteArray(DownloadFileFromAwsS3ByTime.FILE_LOCATION.LAST);

        for (int i = 0; i < bytes.length; i ++) {
            outputStream.write(bytes[i]);
        }
    }
}
