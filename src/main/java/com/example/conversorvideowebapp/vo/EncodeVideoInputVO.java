package com.example.conversorvideowebapp.vo;

import com.example.conversorvideowebapp.enums.VideoWebFormat;

public class EncodeVideoInputVO {
	private String inputUrl;
	private String fileName;
	private String outputUrl;
	private VideoWebFormat videoWebFormat;

	public EncodeVideoInputVO(String inputUrl, String fileName, String outputUrl, VideoWebFormat videoWebFormat) {
		super();
		this.inputUrl = inputUrl;
		this.fileName = fileName;
		this.outputUrl = outputUrl;
		this.videoWebFormat = videoWebFormat;
	}

	public String getInputUrl() {
		return inputUrl;
	}

	public String getFileName() {
		return fileName;
	}

	public String getOutputUrl() {
		return outputUrl;
	}

	public VideoWebFormat getVideoWebFormat() {
		return videoWebFormat;
	}

}
