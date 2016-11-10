package com.kb.service.company;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kb.bean.entity.Company;
import com.kb.service.base.BaseService;

@Service
@Transactional
public class CompanyService extends BaseService<Company, String> {

	public boolean deleteCompanyByPk(String str) {
		defaultDao.deleteByPk(str);
		return true;
	}

	public Company getCompanyByPk(String str) {
		return defaultDao.queryByPk(str);
	}

	public Company saveCompany(Company company) {
		return defaultDao.save(company);
	}
}
