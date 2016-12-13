package com.kb.controller.exception;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("error")
public class ErrorCtrl {

	@RequestMapping("getErrorInfo")
	public Map<String, Object> getErrorInfo() {
		return null;
	}
}
