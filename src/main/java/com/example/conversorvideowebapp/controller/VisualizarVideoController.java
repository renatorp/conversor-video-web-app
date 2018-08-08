package com.example.conversorvideowebapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class VisualizarVideoController {

	/**
	 * Redireciona para a página de visualização de vídeo
	 */
	@GetMapping(path = "/visualizarVideo/{id}")
	public String visualizarVideo(Model model, @PathVariable("id") String videoId) {
		// TODO: model.addAttribute("video", service.buscarVideo(id));
		return "visualizar-video";
	}

}
