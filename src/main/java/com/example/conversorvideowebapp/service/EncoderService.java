package com.example.conversorvideowebapp.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.conversorvideowebapp.exception.ApplicationException;
import com.example.conversorvideowebapp.helper.encoder.EncoderOutputRequestFactory;
import com.example.conversorvideowebapp.helper.encoder.OutputFormatStrategy;
import com.example.conversorvideowebapp.vo.EncodeVideoInputVO;
import com.example.conversorvideowebapp.vo.EncoderInputRequestBody;
import com.example.conversorvideowebapp.vo.EncoderOutputRequest;
import com.example.conversorvideowebapp.vo.EncoderResponseBody;
import com.example.conversorvideowebapp.vo.OutputDetailResponseBody;
import com.example.conversorvideowebapp.vo.OutputProgressResponseBody;

@Service
public class EncoderService {

	@Value("${app.zencoder.job.notificationEmail}")
	private String notificationEmail;

	@Value("${app.zencoder.credentials}")
	private String credentials;

	@Value("${app.zencoder.apiKey}")
	private String zencoderApiKey;

	@Value("${app.zencoder.api.url.createJob}")
	private String zencoderCreateJobUrl;

	@Value("${app.zencoder.api.url.outputProgress}")
	private String zencoderOutputProgressUrl;

	@Value("${app.zencoder.api.url.outputDetail}")
	private String zencoderOutputDetailUrl;

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

			ResponseEntity<EncoderResponseBody> response = restTemplate.postForEntity(zencoderCreateJobUrl, entity,
					EncoderResponseBody.class);

			return response.getBody().getOutputs().iterator().next().getId();

		} catch (RestClientException e) {
			throw new ApplicationException("Ocorreu um erro ao invocar operação rest.", e);
		}

	}

	/**
	 * Obtém estado e porcentagem de conclusão do processamento de um output de id
	 * informado.
	 * 
	 * @param outputId
	 * @return
	 * @throws ApplicationException
	 */
	public OutputProgressResponseBody getOutputProgress(String outputId) {

		RestTemplate restTemplate = new RestTemplate();

		HttpEntity<String> entity = new HttpEntity<>(outputId, createGetHeaders());
		String url = zencoderOutputProgressUrl.replace("{id}", outputId);

		ResponseEntity<OutputProgressResponseBody> response = restTemplate.exchange(url, HttpMethod.GET, entity,
				OutputProgressResponseBody.class);

		return response.getBody();
	}

	/**
	 * Obtém detalhes de um output de id informado.
	 * 
	 * @param outputId
	 * @return
	 * @throws ApplicationException
	 */
	public OutputDetailResponseBody getOutputDetail(String outputId) {

		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<String> entity = new HttpEntity<>(outputId, createGetHeaders());

			String url = zencoderOutputDetailUrl.replace("{id}", outputId);

			ResponseEntity<OutputDetailResponseBody> response = restTemplate.exchange(url, HttpMethod.GET, entity,
					OutputDetailResponseBody.class);

			return response.getBody();

		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
				return null;
			}
			throw e;
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

		HttpHeaders headers = createPostHeaders();

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
	 * Cria cabeçalhos segundo requisitado pela api do zencoder quando método é
	 * POST.
	 * 
	 * @return
	 */
	private HttpHeaders createPostHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", "application/json");
		headers.add("Content-Type", "application/json");
		headers.add("Zencoder-Api-Key", zencoderApiKey);
		return headers;
	}

	/**
	 * Cria cabeçalhos segundo requisitado pela api do zencoder quando método é GET.
	 * 
	 * @return
	 */
	private HttpHeaders createGetHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Zencoder-Api-Key", zencoderApiKey);
		return headers;
	}
}
