package com.kb.dao.base;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.kb.bean.base.BaseDto;
import com.kb.bean.base.BaseEntity;
import com.kb.dao.base.support.HibernateSupport;
import com.kb.dao.base.support.HibernateSupport.HqlFunction;
import com.kb.dao.base.support.Order;
import com.kb.exception.base.BaseRunTimeException;
import com.kb.utils.DeveloperUtils;
import com.kb.utils.date.DateFormat;

/**
 * 基礎 HibernateDao 行為實作
 * 
 * @author KB
 * @version 1.0
 * @see com.kb.dao.base.BaseDao
 */
public class BaseHibernateDaoImpl<T extends BaseEntity, K extends Serializable>
		implements
			BaseHibernateDao<T, K> {

	/**
	 * HQL 中繼類別
	 * 
	 * @author KB
	 * @version 1.0
	 */
	private class HqlRecord {

		private String hqlGenRecord;

		private T params;

		private Map<String, Object> queryMap;

		public HqlRecord(T params, String hqlGenRecord,
				Map<String, Object> queryMap) {
			super();
			this.hqlGenRecord = hqlGenRecord;
			this.params = params;
			this.queryMap = queryMap;
		}

		public String getHqlGenRecord() {
			return hqlGenRecord;
		}

		public T getParams() {
			return params;
		}

		public Map<String, Object> getQueryMap() {
			return queryMap;
		}
	}

	private static Logger logger = LoggerFactory
			.getLogger(BaseHibernateDaoImpl.class);

	@Value("${env.hibernate.alias}")
	private String alias;

	private SessionFactory sessionFactory;

	public BaseHibernateDaoImpl() {
	}

	@Override
	public void delete(T t) {
		getSession().delete(t);
	}

	@Override
	public void deleteByPk(K k) {
		Session session = getSession();
		Object entity = session.get(DeveloperUtils.getGenericClass(this, 0), k);
		if (entity != null) {
			session.delete(entity);
		}
	}

	@Override
	public void flush() {
		getSession().flush();
	}

	@Override
	public <D extends BaseDto> SQLQuery getDtoNamedQuery(String sqlNamed,
			Map<String, Object> queryMap, Class<D> clazz) {
		SQLQuery namedQuery = getNamedSQLQuery(sqlNamed);
		HibernateSupport.bindingDtoScalar(namedQuery, clazz);
		namedQuery.setResultTransformer(Transformers.aliasToBean(clazz));
		if (MapUtils.isNotEmpty(queryMap)) {
			namedQuery.setProperties(queryMap);
		}
		return namedQuery;
	}

	@Override
	public SQLQuery getEntityNamedQuery(String sqlNamed,
			Map<String, Object> queryMap) {
		SQLQuery namedQuery = getNamedSQLQuery(sqlNamed);
		namedQuery.addEntity(DeveloperUtils.getGenericClass(this, 0));
		if (MapUtils.isNotEmpty(queryMap)) {
			namedQuery.setProperties(queryMap);
		}
		return namedQuery;
	}

	@Override
	public Query getHqlNamedQuery(String sqlNamed) {
		Session currentSession = getSession();
		Query namedQuery = currentSession.getNamedQuery(sqlNamed);
		return currentSession.createQuery(namedQuery.getQueryString());
	}

	@Override
	public SQLQuery getMapNamedQuery(String sqlNamed,
			Map<String, Object> queryMap) {
		SQLQuery namedQuery = getNamedSQLQuery(sqlNamed);
		namedQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		if (MapUtils.isNotEmpty(queryMap)) {
			namedQuery.setProperties(queryMap);
		}
		return namedQuery;
	}

	@Override
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@Autowired
	public void initSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T merge(T t) {
		return (T) getSession().merge(t);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> query(HqlFunction hqlFunction, Order... orders) {
		Query hqlQuery = null;
		try {
			Class<T> clazz = (Class<T>) DeveloperUtils.getGenericClass(this, 0);
			hqlQuery = getHqlQuery(
					hqlGenerator(clazz.newInstance(), hqlFunction, orders));
		} catch (InstantiationException | IllegalAccessException e) {
			String errorMsg = String.format(
					"BaseHibernateDao.query has internal error because ... %s",
					e.getMessage());
			logger.error(errorMsg);
			throw new BaseRunTimeException(errorMsg);
		}
		return hqlQuery.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> query(T t, HqlFunction hqlFunction, Order... orders) {
		Query hqlQuery = getHqlQuery(hqlGenerator(t, hqlFunction, orders));
		return hqlQuery.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> query(T t, Order... orders) {
		Query hqlQuery = getHqlQuery(hqlGenerator(t, null, orders));
		return hqlQuery.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> queryAll(Order... orders) {
		Class<T> clazz = (Class<T>) DeveloperUtils.getGenericClass(this, 0);
		try {
			return query(clazz.newInstance(), orders);
		} catch (InstantiationException | IllegalAccessException e) {
			String errorMsg = String.format(
					"BaseHibernateDao.query has internal error because ... %s",
					e.getMessage());
			logger.error(errorMsg);
			throw new BaseRunTimeException(errorMsg);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> queryByMap(Map<String, Object> dataMap,
			HqlFunction hqlFunction, Order... orders) {
		T entityFromMap = getEntityFromMap(dataMap);
		Query hqlQuery = getHqlQuery(
				hqlGenerator(entityFromMap, hqlFunction, orders));
		return hqlQuery.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> queryByMap(Map<String, Object> dataMap, Order... orders) {
		T entityFromMap = getEntityFromMap(dataMap);
		Query hqlQuery = getHqlQuery(hqlGenerator(entityFromMap, null, orders));
		return hqlQuery.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T queryByPk(K k) {
		return (T) getSession().get(DeveloperUtils.getGenericClass(this, 0), k);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T queryLazyInstanceByPk(K k) {
		return (T) getSession().load(DeveloperUtils.getGenericClass(this, 0),
				k);
	}

	@Override
	public T save(T t) {
		getSession().save(t);
		return t;
	}

	@Override
	public T saveOrUpdate(T t) {
		getSession().saveOrUpdate(t);
		return t;
	}

	@Override
	public T update(T t) {
		getSession().update(t);
		return t;
	}

	private void appendDesc(StringBuffer sb, List<String> descList) {
		for (String desc : descList) {
			sb.append(String.format("and %s ", desc));
		}
	}

	@SuppressWarnings("unchecked")
	private T getEntityFromMap(Map<String, Object> dataMap) {
		Class<T> genericClass = (Class<T>) DeveloperUtils.getGenericClass(this,
				0);
		return DeveloperUtils.mapToBean(dataMap, genericClass);
	}

	/**
	 * 產生 Query 物件
	 * 
	 * @param hqlRecord
	 * @return
	 */
	private Query getHqlQuery(HqlRecord hqlRecord) {
		Query result = getSession().createQuery(hqlRecord.getHqlGenRecord());

		// entity query properties
		result.setProperties(hqlRecord.getParams());

		// map query properties
		Map<String, Object> queryMap = hqlRecord.getQueryMap();
		if (MapUtils.isNotEmpty(queryMap)) {
			result.setProperties(queryMap);
		}

		return result;
	}

	private SQLQuery getNamedSQLQuery(String sqlNamed) {
		Session currentSession = getSession();
		String queryString = currentSession.getNamedQuery(sqlNamed)
				.getQueryString();
		return currentSession.createSQLQuery(queryString);
	}

	/**
	 * 產生HQL
	 * 
	 * @param t
	 * @param hqlFunction
	 * @param orders
	 * @return
	 */
	private HqlRecord hqlGenerator(T t, HqlFunction hqlFunction,
			Order... orders) {
		StringBuffer sbHqlGenRecord = new StringBuffer();
		StringBuffer sbHqlGenActualRecord = new StringBuffer();
		String entityName = t.getClass().getSimpleName();

		// 產生標準語法HQL
		String standardQuery = "from %s as %s where 1=1 ";
		sbHqlGenRecord.append(String.format(standardQuery, entityName, alias));
		sbHqlGenActualRecord
				.append(String.format(standardQuery, entityName, alias));

		// 產生屬性查詢HQL
		Map<String, Serializable> beanMap = DeveloperUtils
				.beanToSerializableMap(t);
		if (MapUtils.isNotEmpty(beanMap)) {
			String field;
			Serializable value, cloneValue;
			for (Entry<String, Serializable> set : beanMap.entrySet()) {
				value = set.getValue();
				if (value != null) {
					cloneValue = SerializationUtils.clone(value);
					if (cloneValue instanceof Date) {
						cloneValue = DeveloperUtils.timeFormat(
								(Date) cloneValue, DateFormat.TIMESTAMP);
					}

					field = set.getKey();
					sbHqlGenRecord.append(String.format("and %s.%s = :%s ",
							alias, field, field));
					sbHqlGenActualRecord.append(String.format(
							"and %s.%s = '%s' ", alias, field, cloneValue));
				}
			}
		}

		// 產生函式查詢HQL
		Map<String, Object> queryMap = null;
		if (hqlFunction != null) {
			appendDesc(sbHqlGenRecord, hqlFunction.getHqlFunctionRecord());
			appendDesc(sbHqlGenActualRecord,
					hqlFunction.getHqlFunctionActualRecord());
			queryMap = hqlFunction.getQueryMap();
		}

		// 產生排序HQL
		if (ArrayUtils.isNotEmpty(orders)) {
			String orderBy = "order by ";
			sbHqlGenRecord.append(orderBy);
			sbHqlGenActualRecord.append(orderBy);

			String orderByTemplate0 = "%s.%s %s ";
			String orderByTemplate1 = ",%s.%s %s ";

			Order order;
			for (int i = 0; i < orders.length; i++) {
				order = orders[i];
				if (i != 0) {
					sbHqlGenRecord.append(String.format(orderByTemplate1, alias,
							order.getFieldName(),
							order.getOrderSup().getSort()));
					sbHqlGenActualRecord.append(String.format(orderByTemplate1,
							alias, order.getFieldName(),
							order.getOrderSup().getSort()));
				}
				sbHqlGenRecord.append(String.format(orderByTemplate0, alias,
						order.getFieldName(), order.getOrderSup().getSort()));
				sbHqlGenActualRecord.append(String.format(orderByTemplate0,
						alias, order.getFieldName(),
						order.getOrderSup().getSort()));
			}
		}

		// 開啟 debug 可以看到產出 HQL 實際字串（由字串表達參數）
		logger.info("BaseHibernateDao.hqlNormalGenerator hql format ... {}",
				sbHqlGenRecord.toString());
		logger.debug(
				"BaseHibernateDao.hqlNormalGenerator hql actual format ... {}",
				sbHqlGenActualRecord.toString());

		return new HqlRecord(t, sbHqlGenRecord.toString(), queryMap);
	}
}
