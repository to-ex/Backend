package com.example.toex.common.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${cloud.aws.bucket}")
    private String bucket;
    private final AmazonS3Client s3Client;

    public String uploadFile(MultipartFile multipartFile) throws IOException {
        String fileName = getUuidFileName();

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());

            s3Client.putObject(bucket, fileName, multipartFile.getInputStream(), metadata);
            log.info("File upload success");

        } catch (Exception e) {
            e.printStackTrace();
            log.error("File upload failed : {}", e.getMessage());
        }
        return s3Client.getUrl(bucket, fileName).toString();
    }

    public void deleteImage() {

    }

    public static String getUuidFileName() {
        return UUID.randomUUID().toString();
    }
}
