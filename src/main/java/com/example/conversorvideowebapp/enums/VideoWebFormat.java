package com.example.conversorvideowebapp.enums;

public enum VideoWebFormat {

	MP4(1, "mp4"), OGG(2, "ogg"), WEBM(3, "webm");

	private Integer key;
	private String extension;

	private VideoWebFormat(Integer key, String extension) {
		this.extension = extension;
		this.key = key;
	}

	public Integer getKey() {
		return key;
	}

	public String getExtension() {
		return extension;
	}

	public static VideoWebFormat value(Integer key) {
		for (VideoWebFormat formatEnum : VideoWebFormat.values()) {
			if (formatEnum.getKey().equals(key)) {
				return formatEnum;
			}
		}
		return null;
	}

}
