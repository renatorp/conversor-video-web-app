package com.example.conversorvideowebapp.exception;

public class ApplicationException extends Exception {

	private static final long serialVersionUID = -2009393492671493704L;

	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationException(String message) {
		super(message);
	}
}
