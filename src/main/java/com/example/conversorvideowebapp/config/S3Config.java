package com.example.conversorvideowebapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Config {

	@Value("${app.s3.accessKey}")
	private String awsId;

	@Value("${app.s3.secretKey}")
	private String awsKey;

	@Value("${app.s3.region}")
	private String awsRegion;

	@Bean
	public AmazonS3 s3client() {
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsId, awsKey);
		AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion(awsRegion)
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();

		return s3client;
	}

}
