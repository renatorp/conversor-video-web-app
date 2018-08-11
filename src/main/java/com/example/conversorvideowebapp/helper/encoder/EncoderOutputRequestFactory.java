package com.example.conversorvideowebapp.helper.encoder;

import com.example.conversorvideowebapp.vo.EncodeVideoInputVO;
import com.example.conversorvideowebapp.vo.EncoderOutputRequest;

public class EncoderOutputRequestFactory {

	private OutputFormatStrategy formatStrategy;

	private EncoderOutputRequestFactory(OutputFormatStrategy formatStrategy) {
		this.formatStrategy = formatStrategy;
	}

	public static EncoderOutputRequestFactory create(OutputFormatStrategy formatStrategy) {
		return new EncoderOutputRequestFactory(formatStrategy);
	}

	public EncoderOutputRequest createEncoderOutputRequest(EncodeVideoInputVO inputData, String credentials) {

		EncoderOutputRequest output = createPlainEncoderOutputRequst(inputData.getOutputUrl(), credentials);

		output.setFilename(inputData.getFileName() + "." + inputData.getVideoWebFormat().getExtension());

		formatStrategy.defineFormatEncoderOutputRequest(output);

		return output;
	}

	/**
	 * Cria requisição de output público sem formato específico.
	 * 
	 * @param outputUrl
	 * @param fileName
	 * @return
	 */
	private EncoderOutputRequest createPlainEncoderOutputRequst(String outputUrl, String credentials) {
		EncoderOutputRequest output = new EncoderOutputRequest();
		output.setCredentials(credentials);
		output.setUrl(outputUrl);
		output.setPublic(true);
		return output;
	}

}
