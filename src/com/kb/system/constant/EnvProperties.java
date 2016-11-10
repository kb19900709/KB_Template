package com.kb.system.constant;

import org.springframework.beans.factory.annotation.Value;

/**
 * 對應到 env.properties 的常數設定檔
 * 
 * @author KB
 * @version 1.0
 */
public class EnvProperties {

	@Value("${env.jdbc.driverClassName}")
	protected String driverClassName;

	@Value("${env.hibernate.characterencoding}")
	protected String hibernateCharacterencoding;

	@Value("${env.hibernate.dialect}")
	protected String hibernateDialect;

	@Value("${env.hibernate.showSql}")
	protected String hibernateShowSql;

	@Value("${env.hibernate.useUnicode}")
	protected String hibernateUseUnicode;

	@Value("${env.jdbc.maxActive}")
	protected Integer maxActive;

	@Value("${env.jdbc.maxIdle}")
	protected Integer maxIdle;

	@Value("${env.jdbc.password}")
	protected String password;

	@Value("${env.jdbc.url}")
	protected String url;

	@Value("${env.jdbc.userName}")
	protected String userName;
}
