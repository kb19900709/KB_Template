package com.kb.service.base;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import com.kb.bean.base.BaseEntity;
import com.kb.dao.base.BaseHibernateDaoImpl;
import com.kb.utils.DeveloperUtils;

/**
 * BaseService behavior(Dependency BaseHibernateDao)，將會自動註冊泛型第一個參數的 DAO
 * 
 * @author KB
 * @version 1.0
 * @see com.kb.dao.base.BaseHibernateDaoImpl
 */
public class BaseService<T extends BaseEntity, K extends Serializable> {

	protected BaseHibernateDaoImpl<T, K> defaultDao;

	/**
	 * 是否有自動註冊 defaultDao
	 * 
	 * @return
	 */
	public boolean isDefaultDaoAvailable() {
		return defaultDao != null;
	}

	/**
	 * 自動註冊 DAO by 泛型第一個參數
	 * 
	 * @param appContext
	 */
	@SuppressWarnings("unchecked")
	@Autowired
	private void registerDefaultDaoDao(ApplicationContext appContext) {
		Class<?> currentGenericClass = DeveloperUtils.getGenericFirstClass(this);

		Map<String, Object> beansWithAnnotation = appContext.getBeansWithAnnotation(Repository.class);
		Class<?> repositoryClass;
		Object targetClass;
		for (Entry<String, Object> set : beansWithAnnotation.entrySet()) {
			targetClass = set.getValue();
			repositoryClass = DeveloperUtils.getGenericFirstClass(targetClass);
			if (currentGenericClass.equals(repositoryClass)) {
				this.defaultDao = (BaseHibernateDaoImpl<T, K>) targetClass;
				break;
			}
		}
	}
}
