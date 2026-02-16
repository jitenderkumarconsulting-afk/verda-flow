package com.org.verdaflow.rest.config;

import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.org.verdaflow.rest.config.common.StringConst;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.org.verdaflow.rest.repo")
@PropertySource({ "classpath:application.properties" })
public class DatabaseConfiguration {

	@Autowired
	Environment env;

	@Bean
	public DataSource dataSource() throws PropertyVetoException {
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setDriverClass(env.getProperty(StringConst.DATASOURCE_DRIVERCLASSNAME));
		dataSource.setJdbcUrl(env.getProperty(StringConst.DATASOURCE_URL));
		dataSource.setUser(env.getProperty(StringConst.DATASOURCE_USERNAME));
		dataSource.setPassword(env.getProperty(StringConst.DATASOURCE_PASWORD));

		dataSource.setMaxPoolSize(Integer.parseInt(env.getProperty(StringConst.HIBERNATE_C3P0_MAX_SIZE)));
		dataSource.setMinPoolSize(Integer.parseInt(env.getProperty(StringConst.HIBERNATE_C3P0_MIN_SIZE)));
		dataSource.setMaxStatements(Integer.parseInt(env.getProperty(StringConst.HIBERNATE_C3P0_MAX_STATEMENTS)));
		dataSource.setCheckoutTimeout(Integer.parseInt(env.getProperty(StringConst.HIBERNATE_C3P0_CHECKOUT_TIMEOUT)));
		dataSource.setAcquireRetryAttempts(
				Integer.parseInt(env.getProperty(StringConst.HIBERNATE_C3P0_ACQUIRE_RETRY_ATTEMPTS)));
		dataSource.setTestConnectionOnCheckout(true);
		return dataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws PropertyVetoException {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setPersistenceUnitName("verdaflow");
		em.setDataSource(dataSource());
		em.setPackagesToScan("com.org.verdaflow.rest");
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		em.setJpaProperties(additionalProperties());
		return em;
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);
		return transactionManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	Properties additionalProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		return properties;
	}
}
