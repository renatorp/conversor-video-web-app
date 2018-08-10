package com.example.conversorvideowebapp.controller;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.conversorvideowebapp.helper.FileHelper;
import com.example.conversorvideowebapp.model.Video;
import com.example.conversorvideowebapp.service.S3StorageService;
import com.example.conversorvideowebapp.vo.ConversaoVideoAttribute;

@Controller
public class IndexController {

	@Autowired
	private S3StorageService s3StorageService;

	@Autowired
	private FileHelper fileHelper;

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
	public String converterVideo(@ModelAttribute ConversaoVideoAttribute requestBody) throws IOException {

		// TODO Validar formato de arquivos enviados

		String fileName = requestBody.getFile().getOriginalFilename();

		s3StorageService.uploadMultipartFile(fileHelper.addPathToUploadVideosDirectory(fileName),
				requestBody.getFile());

		// TODO Invocar serviço para converter de formato original para formato destino

		// TODO armazenar arquivo original e arquivo convertido

		return "redirect:/visualizarVideo/" + fileName;
	}

}
