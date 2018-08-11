package com.example.conversorvideowebapp.helper.encoder;

import com.example.conversorvideowebapp.vo.EncoderOutputRequest;

public class OggOutputFormatStrategy implements OutputFormatStrategy {

	@Override
	public void defineFormatEncoderOutputRequest(EncoderOutputRequest vo) {
		vo.setLabel("ogg");
		vo.setFormat("ogg");
	}

}
