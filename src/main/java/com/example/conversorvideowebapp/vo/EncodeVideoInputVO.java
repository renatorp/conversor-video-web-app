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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + ((inputUrl == null) ? 0 : inputUrl.hashCode());
		result = prime * result + ((outputUrl == null) ? 0 : outputUrl.hashCode());
		result = prime * result + ((videoWebFormat == null) ? 0 : videoWebFormat.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EncodeVideoInputVO other = (EncodeVideoInputVO) obj;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (inputUrl == null) {
			if (other.inputUrl != null)
				return false;
		} else if (!inputUrl.equals(other.inputUrl))
			return false;
		if (outputUrl == null) {
			if (other.outputUrl != null)
				return false;
		} else if (!outputUrl.equals(other.outputUrl))
			return false;
		if (videoWebFormat != other.videoWebFormat)
			return false;
		return true;
	}

	
}
