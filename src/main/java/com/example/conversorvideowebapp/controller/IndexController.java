package com.example.conversorvideowebapp.controller;

import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.conversorvideowebapp.model.Video;

@Controller
public class IndexController {

	@RequestMapping(path = "/")
	public String index(Model model) {
		Video video = new Video("1", "Video 1", "mkv");
		model.addAttribute("videos", Arrays.asList(video));
		return "index";
	}

}
