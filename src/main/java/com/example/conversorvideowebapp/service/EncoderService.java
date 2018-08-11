package com.example.conversorvideowebapp.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.conversorvideowebapp.exception.ApplicationException;
import com.example.conversorvideowebapp.helper.encoder.EncoderOutputRequestFactory;
import com.example.conversorvideowebapp.helper.encoder.OutputFormatStrategy;
import com.example.conversorvideowebapp.vo.EncodeVideoInputVO;
import com.example.conversorvideowebapp.vo.EncoderInputRequestBody;
import com.example.conversorvideowebapp.vo.EncoderOutputRequest;
import com.example.conversorvideowebapp.vo.EncoderResponseBody;

@Service
public class EncoderService {

	@Value("${app.zencoder.job.notificationEmail}")
	private String notificationEmail;

	@Value("${app.zencoder.credentials}")
	private String credentials;

	@Value("${app.zencoder.apiKey}")
	private String zencoderApiKey;

	@Value("${app.zencoder.api.url.createJob}")
	private String zencoderUrl;

	/**
	 * Converte video localizado em url informada para um vídeo nos padrões de
	 * visualização da web e o armazena em url de output informada.
	 * 
	 * @param outputUrl
	 * @param fileName
	 * @param videoWebFormat
	 * @param publicUrl
	 * @return
	 * @throws ApplicationException
	 */
	public String encodeVideoForWeb(EncodeVideoInputVO inputData) throws ApplicationException {

		try {

			RestTemplate restTemplate = new RestTemplate();

			HttpEntity<EncoderInputRequestBody> entity = createRequestBody(inputData);

			ResponseEntity<EncoderResponseBody> response = restTemplate.postForEntity(zencoderUrl, entity,
					EncoderResponseBody.class);

			return response.getBody().getOutputs().iterator().next().getUrl();

		} catch (RestClientException e) {
			throw new ApplicationException("Ocorreu um erro ao invocar operação rest.", e);
		}

	}

	/**
	 * Cria corpo da requisição com informações necessárias para efetuar a operação.
	 * 
	 * @param inputUrl
	 * @param outputUrl
	 * @param fileName
	 * @param videoWebFormat
	 * @return
	 */
	private HttpEntity<EncoderInputRequestBody> createRequestBody(EncodeVideoInputVO inputData) {

		HttpHeaders headers = createHeaders();

		EncoderInputRequestBody requestObj = new EncoderInputRequestBody();
		requestObj.setInput(inputData.getInputUrl());
		requestObj.setNotifications(notificationEmail);
		requestObj.setOutput(new ArrayList<>());

		requestObj.getOutput().add(createEncoderOutputRequest(inputData));

		return new HttpEntity<>(requestObj, headers);
	}

	private EncoderOutputRequest createEncoderOutputRequest(EncodeVideoInputVO inputData) {

		EncoderOutputRequestFactory factory = EncoderOutputRequestFactory
				.create(OutputFormatStrategy.of(inputData.getVideoWebFormat()));

		return factory.createEncoderOutputRequest(inputData, credentials);
	}

	/**
	 * Cria cabeçalhos segundo requisitado pela api do zencoder.
	 * 
	 * @return
	 */
	private HttpHeaders createHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", "application/json");
		headers.add("Content-Type", "application/json");
		headers.add("Zencoder-Api-Key", zencoderApiKey);
		return headers;
	}

}
