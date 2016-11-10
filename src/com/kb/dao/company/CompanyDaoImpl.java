package com.kb.dao.company;

import org.springframework.stereotype.Repository;

import com.kb.bean.entity.Company;
import com.kb.dao.base.BaseHibernateDaoImpl;

@Repository
public class CompanyDaoImpl extends BaseHibernateDaoImpl<Company, String> implements CompanyDao {

}
