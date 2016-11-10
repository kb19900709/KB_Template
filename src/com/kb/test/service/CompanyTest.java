package com.kb.test.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import com.kb.bean.entity.Company;
import com.kb.service.company.CompanyService;
import com.kb.system.constant.EnvConstant;
import com.kb.system.web.config.SpringConfig;
import com.kb.test.base.TestBehavior;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfig.class, loader = AnnotationConfigContextLoader.class)
@ActiveProfiles(EnvConstant.ENV_DEV)
@Transactional
public class CompanyTest implements TestBehavior {

	private static Logger logger = LoggerFactory.getLogger(CompanyTest.class);

	private String demoKey = "4028812b57d171120157d17114380000";

	@Autowired
	CompanyService srv;

	@Override
	@Test
	public void autowiredTest() {
		Assert.assertNotNull(srv);
		Assert.assertTrue(srv.isDefaultDaoAvailable());
	}

	@Override
	@Test
	public void deleteTest() {
		boolean result = srv.deleteCompanyByPk(demoKey);
		logger.info("CompanyTest.deleteTest response : {}", result);
		Assert.assertTrue(result);
	}

	@Override
	@Test
	public void queryTest() {
		Company company = srv.getCompanyByPk(demoKey);
		logger.info("CompanyTest.queryTest response : {}", company);
		Assert.assertNotNull(company);
	}

	@Override
	@Test
	public void saveTest() {
		Company company = new Company();
		company.setCompanyName("KB");
		company.setCompanyMail("kb.liao@jetfusion.com.tw");
		company.setCompanyCapitalization(300000);
		company = srv.saveCompany(company);
		logger.info("CompanyTest.saveTest response : {}", company);
		Assert.assertNotNull(company);
	}
}
