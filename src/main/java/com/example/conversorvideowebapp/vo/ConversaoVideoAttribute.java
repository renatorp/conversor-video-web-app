package com.example.conversorvideowebapp.vo;

import org.springframework.web.multipart.MultipartFile;

public class ConversaoVideoAttribute {

	private MultipartFile file;
	private Integer formatoDestino;

	public Integer getFormatoDestino() {
		return formatoDestino;
	}

	public void setFormatoDestino(Integer formatoDestino) {
		this.formatoDestino = formatoDestino;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

}
