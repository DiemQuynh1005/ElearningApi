package com.edu.project_edu.services;

import java.io.InputStream;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class AwsS3OperationService {
  @Value("${aws.s3.access-key}")
  private String accessKey;

  @Value("${aws.s3.secret-key}")
  private String secretKey;

  @Value("${aws.s3.region}")
  private String region;

  @Value("${aws.s3.bucket-name}")
  private String bucketName;

  public String saveFiletoAwsS3Bucket(MultipartFile file) {
    try {
      String s3FileName = new Timestamp(System.currentTimeMillis()).getTime() + "_" + file.getOriginalFilename();
      AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
      AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard().withRegion(region)
          .withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
      InputStream inputStream = file.getInputStream();
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentType("video/mp4");
      PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3FileName, inputStream, metadata);
      amazonS3Client.putObject(putObjectRequest);
      return "https://" + bucketName + ".s3.amazonaws.com/" + s3FileName;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());
    }
  }

  public void deleteFileFromAwsS3Bucket(String fileUrl) {
    try {
      // Extract the object key (filename) from the URL
      String objectKey = extractObjectKeyFromUrl(fileUrl);

      AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
      AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard().withRegion(region)
          .withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();

      amazonS3Client.deleteObject(bucketName, objectKey);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Failed to delete file from S3", e);
    }
  }

  private String extractObjectKeyFromUrl(String fileUrl) {
    int startIndex = fileUrl.indexOf("/", 30); // Start after "s3.amazonaws.com/"
    return fileUrl.substring(startIndex + 1);
  }
}
