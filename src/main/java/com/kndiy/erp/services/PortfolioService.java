package com.kndiy.erp.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@Slf4j
public class PortfolioService {

    public void serveResume(OutputStream outputStream) throws IOException {

        Resource resume = new ClassPathResource("\\static\\files\\Khiemnd9112_Resume.pdf");
        byte[] bytes = resume.getContentAsByteArray();

        for (int i = 0; i < bytes.length; i ++) {
            outputStream.write(bytes[i]);
        }
    }
}
