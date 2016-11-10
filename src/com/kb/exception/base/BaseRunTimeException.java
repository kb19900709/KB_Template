package com.kb.exception.base;

/**
 * 
 * @author KB
 * @version 1.0
 */
public class BaseRunTimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String errorMsg;

	public BaseRunTimeException(String errorMsg) {
		super(errorMsg);
		this.errorMsg = errorMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}
}
