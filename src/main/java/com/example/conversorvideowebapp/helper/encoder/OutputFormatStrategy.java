package com.example.conversorvideowebapp.helper.encoder;

import com.example.conversorvideowebapp.enums.VideoWebFormat;
import com.example.conversorvideowebapp.vo.EncoderOutputRequest;

public interface OutputFormatStrategy {

	void defineFormatEncoderOutputRequest(EncoderOutputRequest vo);

	static OutputFormatStrategy of(VideoWebFormat videoWebFormat) {

		switch (videoWebFormat) {
		case MP4:
			return new Mp4OutputFormatStrategy();
		case WEBM:
			return new WebMOutputFormatStrategy();
		case OGG:
			return new OggOutputFormatStrategy();
		}

		return null;
	}
}
