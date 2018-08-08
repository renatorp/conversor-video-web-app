package com.example.conversorvideowebapp.controller;

import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.conversorvideowebapp.deleteme.Product;

@Controller
public class ProductController {

	@RequestMapping(path = "/")
	public String index(Model model) {
		Product product = new Product("1", "P221", "Aquele Producto", "RETORNÁVEL", "SEM PREÇO", 999999.99D);
		model.addAttribute("products", Arrays.asList(product));
		return "index";
	}

}
