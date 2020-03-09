package com.shorturl.services.exceptions;

public class ShortUrlNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ShortUrlNotFoundException(String message) {
		super(message);
	}
	
	public ShortUrlNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
