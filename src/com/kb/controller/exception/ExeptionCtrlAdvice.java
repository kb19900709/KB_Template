package com.kb.controller.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.kb.controller.base.BaseController;
import com.kb.exception.base.BaseRunTimeException;

@ControllerAdvice
public class ExeptionCtrlAdvice extends BaseController {

	@ExceptionHandler(BaseRunTimeException.class)
	public String processDaoException(BaseRunTimeException e) {
		request.setAttribute("exception", e);
		return "forward:/error/getErrorInfo";
	}
}
