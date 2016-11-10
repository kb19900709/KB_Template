package com.kb.system.web.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.kb.system.constant.EnvConstant;

/**
 * Pure java、spring web setting。傳統 web.xml 替代方案，僅能支援實作 Servlet 3.0 以上的伺服器
 * 
 * @author KB
 * @version 1.0
 */
public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	// Web environment setting
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		super.onStartup(servletContext);
		servletContext.setInitParameter(EnvConstant.SPRING_PROFILES_DEFAULT, EnvConstant.ENV_DEV);
		// servletContext.setInitParameter(EnvConstant.SPRING_PROFILES_ACTIVE,
		// EnvConstant.ENV_PROD);
	}

	// ContextLocationListener application context
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { SpringConfig.class };
	}

	// DispatcherServlet context
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { SpringMvcConfig.class };
	}

	// DispatcherServlet 將接受所有請求
	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

}
