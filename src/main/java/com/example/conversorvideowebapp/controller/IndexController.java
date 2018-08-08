package com.example.conversorvideowebapp.controller;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.example.conversorvideowebapp.controller.vo.ConversaoVideoAttribute;
import com.example.conversorvideowebapp.model.Video;

@Controller
public class IndexController {

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

		MultipartFile arquivoOriginal = requestBody.getFile();

		// TODO Validar formato de arquivos enviados

		// TODO Invocar serviço para converter de formato original para formato destino

		// TODO armazenar arquivo original e arquivo convertido

		return "redirect:/visualizarVideo/1";
	}

}
