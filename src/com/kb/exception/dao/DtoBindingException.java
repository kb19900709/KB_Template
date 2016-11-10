package com.kb.exception.dao;

import com.kb.exception.base.BaseRunTimeException;

/**
 * 
 * @author KB
 * @version 1.0
 */
public class DtoBindingException extends BaseRunTimeException {

	private static final long serialVersionUID = 1L;

	public DtoBindingException(String errorMsg) {
		super(errorMsg);
	}

}
