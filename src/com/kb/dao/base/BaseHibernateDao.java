package com.kb.dao.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.kb.bean.base.BaseDto;
import com.kb.bean.base.BaseEntity;
import com.kb.dao.base.support.HibernateSupport.HqlFunction;
import com.kb.dao.base.support.Order;

/**
 * 基礎 HibernateDao 行為定義
 * 
 * @author KB
 * @version 1.0
 */
public interface BaseHibernateDao<T extends BaseEntity, K extends Serializable> extends BaseDao<T, K> {

	/**
	 * 強制 flush session persistence context
	 */
	void flush();

	/**
	 * 取得回傳物件為 Dto 的 SQLQuery
	 * (setResultTransformer(Transformers.aliasToBean(clazz)))
	 * 
	 * @param sqlNamed
	 *            在 hbm.xml 定義的 sql-query name
	 * @param queryMap
	 *            查詢 Map
	 * @param clazz
	 *            DTO的類別
	 * @return
	 */
	<D extends BaseDto> SQLQuery getDtoNamedQuery(String sqlNamed, Map<String, Object> queryMap, Class<D> clazz);

	/**
	 * 取得回傳物件為 Entity 的 SQLQuery (addEntity(T.class))
	 * 
	 * @param sqlNamed
	 *            在 hbm.xml 定義的 sql-query name
	 * @param queryMap
	 *            查詢 Map
	 * @return
	 */
	SQLQuery getEntityNamedQuery(String sqlNamed, Map<String, Object> queryMap);

	/**
	 * @param sqlNamed
	 *            在 hbm.xml 定義的 sql-query name (HQL)
	 * @return
	 */
	Query getHqlNamedQuery(String sqlNamed);

	/**
	 * 取得回傳物件為 Map 的 SQLQuery (Transformers.ALIAS_TO_ENTITY_MAP)
	 * 
	 * @param sqlNamed
	 *            在 hbm.xml 定義的 sql-query name
	 * @param queryMap
	 *            查詢 Map
	 * @return
	 */
	SQLQuery getMapNamedQuery(String sqlNamed, Map<String, Object> queryMap);

	/**
	 * 取得 Hibernate session
	 * 
	 * @return
	 */
	Session getSession();

	/**
	 * <pre>
	 * Hibernate merge
	 * http://docs.jboss.org/hibernate/orm/3.5/javadocs/org/hibernate/Session.html#merge(java.lang.Object)
	 * </pre>
	 * 
	 * @param t
	 * @return
	 */
	T merge(T t);

	/**
	 * 僅使用部分函示查詢
	 * 
	 * @param clazz
	 * @param hqlFunction
	 * @param orders
	 * @return
	 */
	List<T> query(HqlFunction hqlFunction, Order... orders);

	/**
	 * 使用物件配合部分函示查詢
	 * 
	 * @param t
	 * @param orders
	 * @return
	 */
	List<T> query(T t, HqlFunction hqlFunction, Order... orders);

	/**
	 * 使用 Map 查詢配合部分函示查詢（Map 鍵值須對應到該物件）
	 * 
	 * @param dataMap
	 * @param orders
	 * @return
	 */
	List<T> queryByMap(Map<String, Object> dataMap, HqlFunction hqlFunction, Order... orders);

	/**
	 * 取值僅在 service transaction 有效範圍內可使用
	 * 
	 * @param k
	 * @return
	 */
	T queryLazyInstanceByPk(K k);

	/**
	 * 儲存或是更新物件
	 * 
	 * @param t
	 * @return
	 */
	T saveOrUpdate(T t);
}
