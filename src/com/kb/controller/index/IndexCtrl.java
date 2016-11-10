package com.kb.controller.index;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kb.controller.base.BaseController;

@Controller
public class IndexCtrl extends BaseController {

	@RequestMapping(value = "/")
	public String home() {
		return "index";
	}
}
