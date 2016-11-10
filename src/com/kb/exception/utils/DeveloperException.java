package com.kb.exception.utils;

import com.kb.exception.base.BaseRunTimeException;

/**
 * 
 * @author KB
 * @version 1.0
 */
public class DeveloperException extends BaseRunTimeException {

	private static final long serialVersionUID = 1L;

	public DeveloperException(String errorMsg) {
		super(errorMsg);
	}

}
