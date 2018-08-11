package com.example.conversorvideowebapp.service;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.conversorvideowebapp.vo.EncoderInputRequestBody;
import com.example.conversorvideowebapp.vo.EncoderOutputRequest;
import com.example.conversorvideowebapp.vo.EncoderResponseBody;

@Service
public class EncoderService {

	Logger log = LoggerFactory.getLogger(EncoderService.class);

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
	 * @param publicUrl
	 * @return
	 */
	public String encodeVideoForWeb(String inputUrl, String fileName, String outputUrl) {

		try {

			RestTemplate restTemplate = new RestTemplate();

			HttpEntity<EncoderInputRequestBody> entity = createRequestBody(inputUrl, fileName, outputUrl);

			ResponseEntity<EncoderResponseBody> response = restTemplate.postForEntity(zencoderUrl, entity,
					EncoderResponseBody.class);

			return response.getBody().getOutputs().iterator().next().getUrl();

		} catch (HttpClientErrorException e) {
			log.error("ERROR", e);

		} catch (HttpServerErrorException e) {
			log.error("ERROR", e);

		} catch (ResourceAccessException e) {
			log.error("ERROR", e);

		} catch (RestClientException e) {
			log.error("ERROR", e);
		}

		return null;

	}

	/**
	 * Cria corpo da requisição com informações necessárias para efetuar a operação.
	 * 
	 * @param inputUrl
	 * @param outputUrl
	 * @param fileName
	 * @return
	 */
	private HttpEntity<EncoderInputRequestBody> createRequestBody(String inputUrl, String fileName, String outputUrl) {

		HttpHeaders headers = createHeaders();

		EncoderInputRequestBody requestObj = new EncoderInputRequestBody();
		requestObj.setInput(inputUrl);
		requestObj.setNotifications(notificationEmail);
		requestObj.setOutput(new ArrayList<>());

		requestObj.getOutput().add(createMp4EncoderOutputRequest(outputUrl, fileName));

		return new HttpEntity<>(requestObj, headers);
	}

	/**
	 * Cria requisição de output para formato mp4.
	 * 
	 * @param outputUrl
	 * @param fileName
	 * @return
	 */
	private EncoderOutputRequest createMp4EncoderOutputRequest(String outputUrl, String fileName) {
		EncoderOutputRequest output = createPlainEncoderOutputRequst(outputUrl, fileName);
		output.setFilename(fileName + ".mp4");
		output.setLabel("mp4 high");
		output.setH264_profile("high");
		return output;
	}

	/**
	 * Cria requisição de output para formato webm.
	 * 
	 * @param outputUrl
	 * @param fileName
	 * @return
	 */
	private EncoderOutputRequest createWebmEncoderOutputRequest(String outputUrl, String fileName) {
		EncoderOutputRequest output = createPlainEncoderOutputRequst(outputUrl, fileName);
		output.setFilename(fileName + ".webm");
		output.setLabel("webm");
		output.setFormat("webm");
		return output;
	}

	/**
	 * Cria requisição de output para formato ogg.
	 * 
	 * @param outputUrl
	 * @param fileName
	 * @return
	 */
	private EncoderOutputRequest createOggEncoderOutputRequest(String outputUrl, String fileName) {
		EncoderOutputRequest output = createPlainEncoderOutputRequst(outputUrl, fileName);
		output.setFilename(fileName + ".ogg");
		output.setLabel("ogg");
		output.setFormat("ogg");
		return output;
	}

	/**
	 * Cria requisição de output público sem formato específico.
	 * 
	 * @param outputUrl
	 * @param fileName
	 * @return
	 */
	private EncoderOutputRequest createPlainEncoderOutputRequst(String outputUrl, String fileName) {
		EncoderOutputRequest output = new EncoderOutputRequest();
		output.setCredentials(credentials);
		output.setUrl(outputUrl);
		output.setPublic(true);
		return output;
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
