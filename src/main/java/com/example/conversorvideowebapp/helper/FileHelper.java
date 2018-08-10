package com.example.conversorvideowebapp.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileHelper {

	private static final String SLASH = "/";

	@Value("${app.dir-converted-videos}")
	private String convertedVideosDirName;

	@Value("${app.dir-original-videos}")
	private String originalVideosDirName;

	/**
	 * Converte arquivo MultipartFile para File
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	public String addPathToUploadVideosDirectory(String fileName) {
		return new StringBuilder().append(originalVideosDirName).append(SLASH).append(fileName).toString();
	}

	public String addPathToConvertedVideosDirectory(String fileName) {
		return new StringBuilder().append(convertedVideosDirName).append(SLASH).append(fileName).toString();
	}
}
