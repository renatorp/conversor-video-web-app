package com.example.conversorvideowebapp;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.conversorvideowebapp.controller.IndexController;
import com.example.conversorvideowebapp.enums.VideoWebFormat;
import com.example.conversorvideowebapp.helper.FileHelper;
import com.example.conversorvideowebapp.service.EncoderService;
import com.example.conversorvideowebapp.service.S3StorageService;
import com.example.conversorvideowebapp.vo.ConversaoVideoAttribute;
import com.example.conversorvideowebapp.vo.EncodeVideoInputVO;
import com.example.conversorvideowebapp.vo.OutputDetailResponseBody;

@RunWith(MockitoJUnitRunner.class)
@WebAppConfiguration
public class ConversorVideoWebApplicationTests {

	private MockMvc mockMvc;

	@Mock
	private EncoderService encoderService;

	@Mock
	private S3StorageService s3StorageService;
	
	@Mock
	private FileHelper fileHelper;
	
	private static final String OUTPUT_DIR = "converted";
	private static final String INPUT_DIR = "originals";
	private static final String BASE_INPUT_URL = "http://url.test/";
	private static final String BUCKET_NAME = "bucket";
	
	@Before
	public void setUp() {
		IndexController indexController = new IndexController();
		
		ReflectionTestUtils.setField(indexController, "encoderService", encoderService);
		ReflectionTestUtils.setField(indexController, "s3StorageService", s3StorageService);
		ReflectionTestUtils.setField(indexController, "fileHelper", fileHelper);
		ReflectionTestUtils.setField(indexController, "outputDir", OUTPUT_DIR);
		ReflectionTestUtils.setField(indexController, "inputDir", INPUT_DIR);
		
		mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
	}

	@Test
	public void showVideo_shouldFindVideo() throws Exception {

		OutputDetailResponseBody value = new OutputDetailResponseBody();
		value.setUrl("http://localhost:8080/video/location");
		when(encoderService.getOutputDetail("123456")).thenReturn(value);

		mockMvc.perform(get("/video/123456"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("outputId", "123456"))
				.andExpect(model().attributeDoesNotExist("error"))
				.andExpect(forwardedUrl("index"));

		verify(encoderService, times(1)).getOutputDetail("123456");
		verifyNoMoreInteractions(encoderService);
	}

	@Test
	public void showVideo_shouldNotFindVideo() throws Exception {

		when(encoderService.getOutputDetail("1")).thenReturn(null);

		mockMvc.perform(get("/video/1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeDoesNotExist("outputId"))
				.andExpect(model().attribute("error", "Vídeo não encontrado!"))
				.andExpect(forwardedUrl("index"));

		verify(encoderService, times(1)).getOutputDetail("1");
		verifyNoMoreInteractions(encoderService);
	}
	
	@Test
	public void converterVideo_shouldRedirectToIndex() throws Exception {
		ConversaoVideoAttribute requestBody = new ConversaoVideoAttribute();
		requestBody.setFormatoDestino(1);

		requestBody.setFile(new MockMultipartFile("video", "video.dv", "video/dv", "content".getBytes())); 
		String filePath = INPUT_DIR + "/video.dv";
		String inputUrl = BASE_INPUT_URL + filePath;
		String outputUrl = "s3://" + BUCKET_NAME + "/" + OUTPUT_DIR + "/video.mp4";
		
		when(s3StorageService.getUrlFile(filePath))
			.thenReturn(inputUrl);
		when(fileHelper.extractName("video.dv"))
			.thenReturn("video");
		
		EncodeVideoInputVO encodeVideoInputVO = new EncodeVideoInputVO(inputUrl, "video", outputUrl, VideoWebFormat.MP4);
		
		when(encoderService.encodeVideoForWeb(encodeVideoInputVO))
			.thenReturn("112233");
		
		when(s3StorageService.getBucketName())
			.thenReturn(BUCKET_NAME);
		
		mockMvc.perform(multipart("/video").flashAttr("requestBody", requestBody))
				.andExpect(redirectedUrl("/video/112233"));
		
		verify(encoderService, times(1)).encodeVideoForWeb(encodeVideoInputVO);
		verify(s3StorageService, times(1)).getBucketName();
		verify(s3StorageService, times(1)).getUrlFile(filePath);
		verify(s3StorageService, times(1)).uploadFile(Mockito.anyString(), Mockito.any(InputStream.class));
		verify(fileHelper, times(1)).extractName("video.dv");
		verifyNoMoreInteractions(encoderService, s3StorageService, fileHelper);
	}

	@Test
	public void converterVideo_shouldRequireFile() throws Exception {

		ConversaoVideoAttribute requestBody = new ConversaoVideoAttribute();
		requestBody.setFormatoDestino(1);

		mockMvc.perform(multipart("/video").flashAttr("requestBody", requestBody))
			.andExpect(model().attribute("novoVideo", requestBody))
			.andExpect(model().attribute("error", "É obrigatório informar um vídeo"))
			.andExpect(forwardedUrl("index"));
		
		verifyNoMoreInteractions(encoderService, s3StorageService, fileHelper);
	}

	@Test
	public void converterVideo_shouldRequireExtension() throws Exception {

		ConversaoVideoAttribute requestBody = new ConversaoVideoAttribute();
		requestBody.setFile(new MockMultipartFile("video", "video.dv", "video/dv", "content".getBytes()));

		mockMvc.perform(multipart("/video").flashAttr("requestBody", requestBody))
			.andExpect(model().attribute("novoVideo", requestBody))
			.andExpect(model().attribute("error", "É obrigatório informar o formato de destino"))
			.andExpect(forwardedUrl("index"));
		
		verifyNoMoreInteractions(encoderService, s3StorageService, fileHelper);
	}

	@Test
	public void converterVideo_shouldNotAcceptInvalidFile() throws Exception {

		ConversaoVideoAttribute requestBody = new ConversaoVideoAttribute();
		requestBody.setFormatoDestino(1);
		requestBody.setFile(new MockMultipartFile("video", "video", "video/dv", "content".getBytes()));

		mockMvc.perform(multipart("/video").flashAttr("requestBody", requestBody))
			.andExpect(model().attribute("novoVideo", requestBody))
			.andExpect(model().attribute("error", "Arquivo inválido"))
			.andExpect(forwardedUrl("index"));
		
		verifyNoMoreInteractions(encoderService, s3StorageService, fileHelper);
	}

	@Test
	public void converterVideo_shouldShowGenericErrorMessage() throws Exception {

		ConversaoVideoAttribute requestBody = new ConversaoVideoAttribute();
		requestBody.setFormatoDestino(1);
		requestBody.setFile(new MockMultipartFile("video", "video.dv", "video/dv", "content".getBytes()));
		
		when(s3StorageService.uploadFile(Mockito.anyString(), Mockito.any(InputStream.class)))
			.thenThrow(IOException.class);

		mockMvc.perform(multipart("/video").flashAttr("requestBody", requestBody))
			.andExpect(model().attribute("novoVideo", requestBody))
			.andExpect(model().attribute("error", "Ocorreu um erro inesperado no servidor!"))
			.andExpect(forwardedUrl("index"));
		
		verify(s3StorageService, times(1)).uploadFile(Mockito.anyString(), Mockito.any(InputStream.class));
		verifyNoMoreInteractions(encoderService, s3StorageService, fileHelper);
	}
}
