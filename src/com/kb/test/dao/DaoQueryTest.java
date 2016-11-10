package com.kb.test.dao;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
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

import com.kb.bean.dto.CompanyDto;
import com.kb.bean.entity.Company;
import com.kb.dao.base.support.HibernateSupport;
import com.kb.dao.base.support.HibernateSupport.HqlFunction;
import com.kb.dao.base.support.HqlOperator;
import com.kb.dao.base.support.Order;
import com.kb.dao.base.support.SortEnum;
import com.kb.dao.company.CompanyDao;
import com.kb.system.constant.EnvConstant;
import com.kb.system.web.config.SpringConfig;

/**
 * DAO usage (Company)
 * 
 * @author KB
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfig.class, loader = AnnotationConfigContextLoader.class)
@ActiveProfiles(EnvConstant.ENV_DEV)
@Transactional
public class DaoQueryTest {

	private static Logger logger = LoggerFactory.getLogger(DaoQueryTest.class);

	private String demoKey = "4028812b57d171120157d17114380000";

	@Autowired
	CompanyDao dao;

	@Test
	public void compositeQueryTest1() {
		Company company = new Company();
		company.setCompanyName("傑亮資訊股份有限公司");
		HqlFunction hqlFunction = HibernateSupport.getNewHqlFunction(Company.class);
		hqlFunction.addCondition("createdUser", HqlOperator.NOT_IN, new String[] { "KB", "Rick" })
				.addCondition("companyCapitalization", HqlOperator.IN, Arrays.asList(10000000, 20000000));
		List<Company> result = dao.query(company, hqlFunction, Order.asOrder("createdDate", SortEnum.ASC));
		logger.info("DaoQueryTest.compositeQueryTest1 response : {}", result);
		Assert.assertEquals(result.get(0).getCompanyName(), "傑亮資訊股份有限公司");
	}

	@Test
	public void compositeQueryTest2() {
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("companyName", "傑亮資訊股份有限公司");
		HqlFunction hqlFunction = HibernateSupport.getNewHqlFunction(Company.class);
		hqlFunction.addCondition("createdUser", HqlOperator.NOT_IN, new String[] { "KB", "Rick" })
				.addCondition("companyCapitalization", HqlOperator.IN, Arrays.asList(10000000, 20000000));
		List<Company> result = dao.queryByMap(queryParams, hqlFunction, Order.asOrder("createdDate", SortEnum.DESC));
		logger.info("DaoQueryTest.compositeQueryTest2 response : {}", result);
		Assert.assertEquals(result.get(0).getCompanyName(), "傑亮資訊股份有限公司");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void customerHqlQueryTest1() {
		Query query = dao.getHqlNamedQuery("hql.company.getAllCompany");
		List<Company> result = query.list();
		logger.info("DaoQueryTest.customerHqlQueryTest1 response : {}", result);
		Assert.assertEquals(result.size(), 2);
	}

	@Test
	public void customerHqlQueryTest2() {
		Company queryEntity = new Company();
		queryEntity.setCompanyCapitalization(10000000);
		queryEntity.setModifyUser("Max");
		Query query = dao.getHqlNamedQuery("hql.company.getCompanyByCompanyCapitalization");
		query.setProperties(queryEntity);
		Company result = (Company) query.uniqueResult();
		logger.info("DaoQueryTest.customerHqlQueryTest2 response : {}", result);
		Assert.assertEquals(result.getCompanyName(), "傑亮資訊股份有限公司");
	}

	@Test
	public void entityQueryTest1() {
		Company company = new Company();
		company.setCompanyCapitalization(10000000);
		company.setCompanyName("傑亮資訊股份有限公司");
		List<Company> result = dao.query(company);
		logger.info("DaoQueryTest.entityQueryTest1 response : {}", result);
		Assert.assertEquals(result.get(0).getCompanyName(), "傑亮資訊股份有限公司");
	}

	@Test
	public void entityQueryTest2() {
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("companyCapitalization", 10000000);
		queryParams.put("companyName", "傑亮資訊股份有限公司");
		List<Company> result = dao.queryByMap(queryParams);
		logger.info("DaoQueryTest.entityQueryTest2 response : {}", result);
		Assert.assertEquals(result.get(0).getCompanyName(), "傑亮資訊股份有限公司");
	}

	@Test
	public void hqlFunctionTest1() {
		HqlFunction hqlFunction = HibernateSupport.getNewHqlFunction(Company.class);
		hqlFunction.addCondition("companyCapitalization", HqlOperator.LE, 20000000);
		List<Company> result = dao.query(hqlFunction);
		logger.info("DaoQueryTest.hqlFunctionTest1 response : {}", result);
		Assert.assertEquals(result.get(0).getCompanyName(), "傑亮資訊股份有限公司");
	}

	@Test
	public void hqlFunctionTest2() {
		HqlFunction hqlFunction = HibernateSupport.getNewHqlFunction(Company.class);
		hqlFunction.addCondition("companyCapitalization", HqlOperator.LE, 20000000).addCondition("companyMail",
				HqlOperator.EQ, "jetfusionAdmin@jetfusion.com.tw");
		List<Company> result = dao.query(hqlFunction);
		logger.info("DaoQueryTest.hqlFunctionTest2 response : {}", result);
		Assert.assertEquals(result.get(0).getCompanyMail(), "jetfusionAdmin@jetfusion.com.tw");
	}

	@Test
	public void hqlFunctionTest3() {
		HqlFunction hqlFunction = HibernateSupport.getNewHqlFunction(Company.class);
		hqlFunction.addCondition("companyCapitalization", HqlOperator.BETWEEN, 40000000, 70000000)
				.addCondition("companyMail", HqlOperator.EQ, "IBM")
				.orPreCondition("modifyUser", HqlOperator.EQ, "admin");
		List<Company> result = dao.query(hqlFunction);
		logger.info("DaoQueryTest.hqlFunctionTest3 response : {}", result);
		Assert.assertEquals(result.get(0).getCompanyName(), "昕力資訊股份有限公司");
	}

	@Test
	public void hqlFunctionTest4() {
		HqlFunction hqlFunction = HibernateSupport.getNewHqlFunction(Company.class);
		hqlFunction.addCondition("companyCapitalization", HqlOperator.BETWEEN, 40000000, 70000000)
				.addCondition("companyMail", HqlOperator.EQ, "IBM").orPreCondition("modifyUser", HqlOperator.EQ, "max")
				.orPreCondition("modifyUser", HqlOperator.EQ, "admin");
		List<Company> result = dao.query(hqlFunction);
		logger.info("DaoQueryTest.hqlFunctionTest4 response : {}", result);
		Assert.assertEquals(result.get(0).getCompanyName(), "昕力資訊股份有限公司");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void nativeSqlQueryTest1() {
		SQLQuery query = dao.getEntityNamedQuery("sql.company.getAllCompany", null);
		List<Company> result = query.list();
		logger.info("DaoQueryTest.nativeSqlQueryTest1 response : {}", result);
		Assert.assertEquals(result.size(), 2);
	}

	@Test
	public void nativeSqlQueryTest2() {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("company_capitalization", 10000000);
		queryMap.put("mu", "Max");
		SQLQuery query = dao.getEntityNamedQuery("sql.company.getCompanyByCompanyCapitalization", queryMap);
		Company result = (Company) query.uniqueResult();
		logger.info("DaoQueryTest.nativeSqlQueryTest2 response : {}", result);
		Assert.assertEquals(result.getCompanyName(), "傑亮資訊股份有限公司");
	}

	@Test
	public void nativeSqlQueryTest3() {
		SQLQuery query = dao.getDtoNamedQuery("sql.company.getCompanyInfo", null, CompanyDto.class);
		CompanyDto result = (CompanyDto) query.uniqueResult();
		logger.info("DaoQueryTest.nativeSqlQueryTest3 response : {}", result);
		Assert.assertEquals(result.getTotal(), new Integer(2));
	}

	@Test
	public void nativeSqlQueryTest4() {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("mu", "Max");
		SQLQuery query = dao.getDtoNamedQuery("sql.company.getCompanyInfoByUser", queryMap, CompanyDto.class);
		CompanyDto result = (CompanyDto) query.uniqueResult();
		logger.info("DaoQueryTest.nativeSqlQueryTest4 response : {}", result);
		Assert.assertEquals(result.getTotal(), new Integer(1));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void nativeSqlQueryTest5() {
		SQLQuery query = dao.getMapNamedQuery("sql.company.getAllCompany", null);
		List<Map<String, Object>> result = query.list();
		logger.info("DaoQueryTest.nativeSqlQueryTest5 response : {}", result);
		Assert.assertEquals(result.size(), 2);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void nativeSqlQueryTest6() {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("company_capitalization", 10000000);
		queryMap.put("mu", "Max");
		SQLQuery query = dao.getMapNamedQuery("sql.company.getCompanyByCompanyCapitalization", queryMap);
		Map<String, Object> result = (Map<String, Object>) query.uniqueResult();
		logger.info("DaoQueryTest.nativeSqlQueryTest6 response : {}", result);
		Assert.assertEquals(result.get("company_name"), "傑亮資訊股份有限公司");
	}

	@Test
	public void queryAllTest() {
		List<Company> result = dao.queryAll(Order.asOrder("companyCapitalization", SortEnum.DESC));
		logger.info("DaoQueryTest.queryAllTest response : {}", result);
		Assert.assertEquals(result.size(), 2);
	}

	@Test
	public void queryByPkTest() {
		Company result = dao.queryByPk(demoKey);
		logger.info("DaoQueryTest.queryByPkTest response : {}", result);
		Assert.assertEquals(result.getCompanyName(), "傑亮資訊股份有限公司");
	}

	@Test
	public void queryLazyInstanceByPkTest() {
		Company result = dao.queryLazyInstanceByPk(demoKey);
		logger.info("DaoQueryTest.queryLazyInstanceByPkTest response : {}", result);
		Assert.assertEquals(result.getCompanyName(), "傑亮資訊股份有限公司");
	}
}
