package com.example.conversorvideowebapp.controller;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.example.conversorvideowebapp.enums.VideoWebFormat;
import com.example.conversorvideowebapp.exception.ApplicationException;
import com.example.conversorvideowebapp.exception.ValidationException;
import com.example.conversorvideowebapp.helper.FileHelper;
import com.example.conversorvideowebapp.service.EncoderService;
import com.example.conversorvideowebapp.service.S3StorageService;
import com.example.conversorvideowebapp.vo.ConversaoVideoAttribute;
import com.example.conversorvideowebapp.vo.EncodeVideoInputVO;

@Controller
public class IndexController {

	private static final Logger log = LoggerFactory.getLogger(IndexController.class);

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

	@GetMapping(path = "/video/{fileName}/{ext}")
	public String index(@PathVariable("fileName") String fileName, @PathVariable("ext") String ext, Model model) {
		model.addAttribute("novoVideo", new ConversaoVideoAttribute());

		String urlFile = s3StorageService.getUrlFile(outputDir + "/" + fileName + "." + ext);
		model.addAttribute("videoUrl", urlFile);
		model.addAttribute("videoContentType", "video/" + ext);

		return "index";
	}

	@PostMapping(path = "/videos/converter")
	public ModelAndView converterVideo(Model model, @ModelAttribute ConversaoVideoAttribute requestBody,
			RedirectAttributes redirectAttributes) throws IOException {

		try {
			validateRequest(requestBody);

			String originalFileName = requestBody.getFile().getOriginalFilename();

			String inputUrl = uploadToS3Storage(requestBody.getFile(), originalFileName);

			VideoWebFormat videoFormat = VideoWebFormat.value(requestBody.getFormatoDestino());
			String fileName = fileHelper.extractName(originalFileName);

			encodeAndStoreVideo(fileName, inputUrl, videoFormat);

			// Redireciona para index
			RedirectView redirectView = new RedirectView();
			redirectView.setUrl("/video/" + fileName + "/" + videoFormat.getExtension());

			return new ModelAndView(redirectView);

		} catch (ValidationException e) {
			model.addAttribute("novoVideo", requestBody);
			model.addAttribute("error", e.getMessage());
			return new ModelAndView("index");

		} catch (ApplicationException e) {
			log.error(e.getMessage(), e);
			model.addAttribute("novoVideo", requestBody);
			model.addAttribute("error", "Ocorreu um erro inesperado no servidor!");
			return new ModelAndView("index");
		}
	}

	/**
	 * Aciona servico de conversão passando a url do video de origem e a extensão
	 * indicada. O serviço retornará a url de acesso imediato ao vídeo
	 * 
	 * @param fileName
	 * @param inputUrl
	 * @param videoWebFormat
	 * @return
	 * @throws ApplicationException
	 */
	private String encodeAndStoreVideo(String fileName, String inputUrl, VideoWebFormat videoWebFormat)
			throws ApplicationException {

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
	 * @throws ApplicationException
	 */
	private String uploadToS3Storage(MultipartFile multipartFile, String fileName) throws ApplicationException {

		File file = fileHelper.convertMultiPartToFile(multipartFile);

		try {
			validateVideoFile(file);

			String filePath = inputDir + "/" + fileName;

			s3StorageService.uploadFile(filePath, file);

			return s3StorageService.getUrlFile(filePath);

		} finally {
			file.delete(); // Deleta arquivo temporário
		}
	}

	/**
	 * Valida se arquivo informado é de vídeo
	 * 
	 * @param file
	 * @throws ApplicationException
	 */
	private void validateVideoFile(File file) throws ApplicationException {
		try {
			if (!java.nio.file.Files.probeContentType(file.toPath()).startsWith("video/")) {
				throw new ValidationException("Arquivo inválido!");
			}
		} catch (IOException e) {
			throw new ApplicationException("Erro ao analizar arquivo", e);
		}

	}

	/**
	 * Valida inputs obrigatórios
	 * 
	 * @param requestBody
	 * @throws ValidationException
	 */
	private void validateRequest(ConversaoVideoAttribute requestBody) throws ValidationException {

		if (requestBody.getFile() == null || StringUtils.isEmpty(requestBody.getFile().getOriginalFilename())) {
			throw new ValidationException("É obrigatório informar um vídeo");
		}

		if (requestBody.getFormatoDestino() == null) {
			throw new ValidationException("É obrigatório informar o formato de destino");
		}

		if (!requestBody.getFile().getOriginalFilename().contains(".")) {
			throw new ValidationException("Arquivo inválido");
		}
	}

}
