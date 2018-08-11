package com.example.conversorvideowebapp.service;

import java.io.File;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

@Service
public class S3StorageService {

	@Autowired
	private AmazonS3 s3client;

	@Value("${app.s3.bucketName}")
	private String bucketName;

	/**
	 * Envia arquivo para S3 com visibilidade pública.
	 * 
	 * @param key
	 * @param file
	 * @return
	 */
	public PutObjectResult uploadFile(String key, File file) {
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);

		// Envia arquivo com visibilidade pública.
		putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);

		return s3client.putObject(putObjectRequest);
	}

	/**
	 * Obtém arquivo do S3
	 * 
	 * @param fileName
	 * @return
	 */
	public InputStream downloadFile(String fileName) {
		S3Object s3object = s3client.getObject(new GetObjectRequest(bucketName, fileName));
		return s3object.getObjectContent();
	}

	/**
	 * Obtém url de acesso público a arquivo
	 * 
	 * @param fileName
	 * @return
	 */
	public String getUrlFile(String fileName) {
		return s3client.getUrl(bucketName, fileName).toExternalForm();
	}

	public Object getBucketName() {
		return bucketName;
	}

}
