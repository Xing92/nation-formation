package com.xing.main.exception;

public class WrongDataException extends RuntimeException {

	public WrongDataException() {
		super();
	}

	public WrongDataException(String message) {
		super(message);
	}

	public WrongDataException(String message, Throwable t) {
		super(message, t);
	}

}
