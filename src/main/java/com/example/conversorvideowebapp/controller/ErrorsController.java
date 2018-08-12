package com.example.conversorvideowebapp.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorsController implements ErrorController {

	@RequestMapping(value = "/error", method = RequestMethod.GET)
	public ModelAndView handleError(HttpServletRequest httpRequest) {

		ModelAndView errorPage = new ModelAndView("error-page");

		String errorMsg = "";

		if (getErrorCode(httpRequest) / 100 == 4) {
			errorMsg = "Recurso não disponível!";
		} else {
			errorMsg = "Ocorreu um erro no servidor!";
		}

		errorPage.addObject("errorMsg", errorMsg);

		return errorPage;
	}

	private int getErrorCode(HttpServletRequest httpRequest) {
		return (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}
}
