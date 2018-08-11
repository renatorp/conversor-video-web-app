package com.example.conversorvideowebapp.vo;

public class EncoderOutputRequest {

	private String url;
	private String filename;
	private String credentials;
	private String h264_profile;
	private String label;
	private String format;
	private Boolean _public;

	public EncoderOutputRequest() {
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getCredentials() {
		return credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}

	public void setH264_profile(String h264_profile) {
		this.h264_profile = h264_profile;
	}

	public String getH264_profile() {
		return h264_profile;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public Boolean getPublic() {
		return _public;
	}

	public void setPublic(Boolean _public) {
		this._public = _public;
	}

}
