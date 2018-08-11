package com.example.conversorvideowebapp.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.example.conversorvideowebapp.enums.VideoWebFormat;
import com.example.conversorvideowebapp.helper.FileHelper;
import com.example.conversorvideowebapp.service.EncoderService;
import com.example.conversorvideowebapp.service.S3StorageService;
import com.example.conversorvideowebapp.vo.ConversaoVideoAttribute;
import com.example.conversorvideowebapp.vo.EncodeVideoInputVO;

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

	/**
	 * Exibe página inicial
	 */
	@GetMapping(path = "/")
	public String index(Model model) {
		model.addAttribute("novoVideo", new ConversaoVideoAttribute());
		return "index";
	}

	@PostMapping(path = "/videos/converter")
	public RedirectView converterVideo(Model model, @ModelAttribute ConversaoVideoAttribute requestBody,
			RedirectAttributes redirectAttributes) throws IOException {

		// TODO Validar formato de arquivos enviados

		String originalFileName = requestBody.getFile().getOriginalFilename();

		String inputUrl = uploadToS3Storage(requestBody.getFile(), originalFileName);

		VideoWebFormat videoFormat = VideoWebFormat.value(requestBody.getFormatoDestino());
		String encodeVideoUrl = encodeVideo(fileHelper.extractName(originalFileName), inputUrl, videoFormat);

		// Redireciona para index
		RedirectView redirectView = new RedirectView();
		redirectView.setContextRelative(true);
		redirectAttributes.addFlashAttribute("videoUrl", encodeVideoUrl);
		redirectAttributes.addFlashAttribute("videoContentType", "video/" + videoFormat.getExtension());
		redirectView.setUrl("/");

		return redirectView;
	}

	/**
	 * Aciona servico de conversão passando a url do video de origem e a extensão
	 * indicada. O serviço retornará a url de acesso imediato ao vídeo
	 * 
	 * @param fileName
	 * @param inputUrl
	 * @param videoWebFormat
	 * @return
	 */
	private String encodeVideo(String fileName, String inputUrl, VideoWebFormat videoWebFormat) {

		String outputUrl = new StringBuilder().append("s3://").append(s3StorageService.getBucketName()).append("/")
				.append(outputDir).append("/").append(fileName).append(".").append(videoWebFormat.getExtension())
				.toString();

		return enconderService.encodeVideoForWeb(new EncodeVideoInputVO(inputUrl, fileName, outputUrl, videoWebFormat));
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
