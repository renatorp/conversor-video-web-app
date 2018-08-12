package com.example.conversorvideowebapp.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

//@Controller
public class ErrorsController {

	@GetMapping(path = "error")
	public String handleError(HttpServletRequest httpRequest) {

		ModelAndView errorPage = new ModelAndView("error-page");

		String errorMsg = "";

		if (getErrorCode(httpRequest) == 404) {
			errorMsg = "Página não econtrada!";
		}

//	 
//	        switch (httpErrorCode) {
//	            case 400: {
//	                errorMsg = "Http Error Code: 400. Bad Request";
//	                break;
//	            }
//	            case 401: {
//	                errorMsg = "Http Error Code: 401. Unauthorized";
//	                break;
//	            }
//	            case 404: {
//	                errorMsg = "Http Error Code: 404. Resource not found";
//	                break;
//	            }
//	            case 500: {
//	                errorMsg = "Http Error Code: 500. Internal Server Error";
//	                break;
//	            }
//	        }
//	        errorPage.addObject("errorMsg", errorMsg);
//	        return errorPage;	

		return "error-page";
	}

	private int getErrorCode(HttpServletRequest httpRequest) {
		return (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
	}
}
