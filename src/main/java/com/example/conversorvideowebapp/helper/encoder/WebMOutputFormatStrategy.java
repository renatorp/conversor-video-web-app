package com.example.conversorvideowebapp.helper.encoder;

import com.example.conversorvideowebapp.vo.EncoderOutputRequest;

public class WebMOutputFormatStrategy implements OutputFormatStrategy {

	@Override
	public void defineFormatEncoderOutputRequest(EncoderOutputRequest vo) {
		vo.setLabel("webm");
		vo.setFormat("webm");
	}

}
