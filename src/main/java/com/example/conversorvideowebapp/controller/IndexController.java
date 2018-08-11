package com.example.conversorvideowebapp.controller;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.example.conversorvideowebapp.helper.FileHelper;
import com.example.conversorvideowebapp.model.Video;
import com.example.conversorvideowebapp.service.EncoderService;
import com.example.conversorvideowebapp.service.S3StorageService;
import com.example.conversorvideowebapp.vo.ConversaoVideoAttribute;

@Controller
public class IndexController {

	@Autowired
	private S3StorageService s3StorageService;

	@Autowired
	private FileHelper fileHelper;

	@Autowired
	private EncoderService enconderService;

	@Value("${app.dir-converted-videos}")
	private String outputDir;

	@Value("${app.dir-original-videos}")
	private String inputDir;

	@Value("${app.url-format-s3-output}")
	private String outputUrlTemplate;

	/**
	 * Exibe página inicial
	 */
	@GetMapping(path = "/")
	public String index(Model model) {

		// TODO Remover id de vídeo, utilizar apenas o nome para identificar
		Video video = new Video("1", "Video 1", "mkv");
		model.addAttribute("videos", Arrays.asList(video));
		model.addAttribute("novoVideo", new ConversaoVideoAttribute());
		return "index";
	}

	@PostMapping(path = "/videos/converter")
	public String converterVideo(Model model, @ModelAttribute ConversaoVideoAttribute requestBody) throws IOException {

		// TODO Validar formato de arquivos enviados

		String originalFileName = requestBody.getFile().getOriginalFilename();

		String inputUrl = uploadToS3Storage(requestBody.getFile(), originalFileName);

		String encodeVideoUrl = encodeVideo(fileHelper.extractName(originalFileName), inputUrl, "mp4");

		model.addAttribute("videoUrl", encodeVideoUrl);

		return "redirect:/visualizarVideo/";
	}

	/**
	 * Aciona servico de conversão passando a url do video de origem e a extensão
	 * indicada. O serviço retornará a url de acesso imediato ao vídeo
	 * 
	 * @param fileName
	 * @param inputUrl
	 * @param outputExtension
	 * @return
	 */
	private String encodeVideo(String fileName, String inputUrl, String outputExtension) {

		String outputUrl = new StringBuilder().append("s3://").append(s3StorageService.getBucketName()).append("/")
				.append(outputDir).append("/").append(fileName).append(".").append(outputExtension).toString();

		return enconderService.encodeVideoForWeb(inputUrl, fileName, outputUrl);
	}

	/**
	 * Adiciona vídeo à storage s3 no diretório de inputs com o nome informado.
	 * 
	 * @param file
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	private String uploadToS3Storage(MultipartFile file, String fileName) throws IOException {

		String filePath = inputDir + "/" + fileName;

		s3StorageService.uploadMultipartFile(filePath, file);

		return s3StorageService.getUrlFile(filePath);
	}

}
