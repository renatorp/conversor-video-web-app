package com.example.conversorvideowebapp.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileHelper {

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

	public String extractName(String fileName) {
		return fileName.substring(0, fileName.lastIndexOf("."));
	}
}
