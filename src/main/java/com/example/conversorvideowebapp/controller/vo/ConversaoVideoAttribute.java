package com.example.conversorvideowebapp.controller.vo;

import org.springframework.web.multipart.MultipartFile;

public class ConversaoVideoAttribute {

	private MultipartFile file;
	private String formatoDestino;

	public String getFormatoDestino() {
		return formatoDestino;
	}

	public void setFormatoDestino(String formatoDestino) {
		this.formatoDestino = formatoDestino;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

}
