package com.kb.system.web.config;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.EmptyInterceptor;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.kb.system.constant.EnvConstant;
import com.kb.system.constant.EnvProperties;
import com.kb.system.web.hibernate.HibernateInterceptor;

/**
 * DB connection setting 、 Hibernate setting and Enables Spring's
 * annotation-driven transaction management capability.
 * 
 * @author KB
 * @version 1.0
 */
@Configuration
@EnableTransactionManagement
public class DataSourceConfig extends EnvProperties {

	@Autowired
	private ResourceLoader loader;

	// datasource
	@Bean
	@Profile(EnvConstant.ENV_DEV)
	public BasicDataSource dataSource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driverClassName);
		ds.setUrl(url);
		ds.setUsername(userName);
		ds.setPassword(password);
		ds.setMaxActive(maxActive);
		ds.setMaxIdle(maxIdle);
		return ds;
	}

	// hibernate Interceptor
	@Bean
	public EmptyInterceptor emptyInterceptor() {
		return new HibernateInterceptor();
	}

	// session factory
	@Bean
	public LocalSessionFactoryBean sessionFactory(DataSource ds, EmptyInterceptor interceptor) throws IOException {
		LocalSessionFactoryBean lsfb = new LocalSessionFactoryBean();
		lsfb.setEntityInterceptor(interceptor);
		lsfb.setDataSource(ds);
		lsfb.setPackagesToScan(new String[] { EnvConstant.HIBERNATE_ENTITY });
		// hbm.xml 設定
		lsfb.setMappingLocations(
				ResourcePatternUtils.getResourcePatternResolver(loader).getResources(EnvConstant.HIBERNATE_RESOURCE));
		lsfb.setHibernateProperties(getHibernateProps());
		return lsfb;
	}

	// hibernate transaction manager
	@Bean
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
		return new HibernateTransactionManager(sessionFactory);
	}

	// hibernate properties
	private Properties getHibernateProps() {
		Properties hibernateProps = new Properties();
		hibernateProps.setProperty(EnvConstant.HIBERNATE_DIALECT, hibernateDialect);
		hibernateProps.setProperty(EnvConstant.HIBERNATE_SHOW_SQL, hibernateShowSql);
		hibernateProps.setProperty(EnvConstant.HIBERNATE_CONNECTION_CHARACTERENCODING, hibernateCharacterencoding);
		hibernateProps.setProperty(EnvConstant.HIBERNATE_CONNECTION_USEUNICODE, hibernateUseUnicode);
		return hibernateProps;
	}
}
