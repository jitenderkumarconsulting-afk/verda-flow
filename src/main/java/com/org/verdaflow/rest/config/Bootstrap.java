package com.org.verdaflow.rest.config;

import java.util.Date;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.util.DateUtil;

public class Bootstrap implements WebApplicationInitializer {
	private final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

	public void onStartup(ServletContext servletContext) throws ServletException {
		String dateString = DateUtil.formatDateToddMMyyyyHHmmss(new Date());
		logger.info("********************************************************");
		logger.info("***************** Verda-Flow Server Status ***************");
		logger.info("========================================================");
		logger.info("***** Context Initialized       @  : " + dateString);
		logger.info("***** Server Startup/Reloaded   @  : " + dateString);
		logger.info("========================================================");
		logger.info("********************************************************");

		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(MvcConfiguration.class);
		servletContext.addListener(new ContextLoaderListener(rootContext));
		// Manage the lifecycle of the root application context

		ServletRegistration.Dynamic dispatcher = servletContext.addServlet(StringConst.DISPATCHER,
				new DispatcherServlet(rootContext));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping(StringConst.FILE_SEPARATOR);
		dispatcher.setInitParameter(StringConst.THROW_EXCEPTION_IF_NO_HANDLER_FOUND, StringConst.TRUE);

		FilterRegistration.Dynamic encodingFilter = servletContext.addFilter(StringConst.ENCODING_FILTER,
				new CharacterEncodingFilter());
		encodingFilter.setInitParameter(StringConst.ENCODING, StringConst.UTF_8);
		encodingFilter.setInitParameter(StringConst.FORCE_ENCODING, StringConst.TRUE);
		encodingFilter.addMappingForUrlPatterns(null, true, StringConst.FILE_SEPARATOR_MULTIPLICATION);
	}
}
