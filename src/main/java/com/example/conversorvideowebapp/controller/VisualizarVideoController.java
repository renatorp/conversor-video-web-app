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
	@GetMapping(path = "/visualizarVideo/{fileName}")
	public String visualizarVideo(Model model, @PathVariable("fileName") String fileName) {
		// TODO: buscar de serviço url de video para ser enviada
		return "visualizar-video";
	}

}
