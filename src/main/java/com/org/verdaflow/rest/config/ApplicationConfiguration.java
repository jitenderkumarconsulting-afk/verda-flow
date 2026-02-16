package com.org.verdaflow.rest.config;

import java.io.IOException;
import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

import com.org.verdaflow.rest.config.common.ApplicationContextProvider;

@Configuration
public class ApplicationConfiguration {

	@Bean(name = "applicationContext")
	public ApplicationContextProvider applicationContextProvider() {
		return new ApplicationContextProvider();
	}

	@Bean(name = "applicationEventMulticaster")
	public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
		SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();

		eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
		return eventMulticaster;
	}

	@Bean
	public VelocityEngine velocityEngine() throws IOException {
		VelocityEngineFactoryBean factory = new VelocityEngineFactoryBean();
		Properties props = new Properties();
		props.put("resource.loader", "class");
		props.put("class.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader." + "ClasspathResourceLoader");
		factory.setVelocityProperties(props);
		return factory.createVelocityEngine();
	}
}
