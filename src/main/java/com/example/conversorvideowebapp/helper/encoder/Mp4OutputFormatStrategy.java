package com.example.conversorvideowebapp.helper.encoder;

import com.example.conversorvideowebapp.vo.EncoderOutputRequest;

public class Mp4OutputFormatStrategy implements OutputFormatStrategy {

	@Override
	public void defineFormatEncoderOutputRequest(EncoderOutputRequest vo) {
		vo.setLabel("mp4 high");
		vo.setH264_profile("high");
	}

}
