package com.david.scaffold.util.exception;

public class SimpleException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String code;

	private String message;

	private Throwable ex;

	public SimpleException() {
		super();
	}

	public SimpleException(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public SimpleException(String code, String message, Throwable ex) {
		this.code = code;
		this.message = message;
		this.ex = ex;
	}
	
	public SimpleException(String message, Throwable ex) {
		this.message = message;
		this.ex = ex;
	}
	
	public SimpleException(Throwable ex) {
		this.ex = ex;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Throwable getEx() {
		return ex;
	}

	public void setEx(Throwable ex) {
		this.ex = ex;
	}

}
