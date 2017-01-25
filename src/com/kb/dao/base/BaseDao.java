package com.kb.dao.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.kb.bean.base.BaseEntity;
import com.kb.dao.base.support.Order;

/**
 * 基礎 DAO 行為定義
 * 
 * @author KB
 * @version 1.0
 */
public interface BaseDao<T extends BaseEntity, K extends Serializable> {

	/**
	 * 使用物件刪除
	 * 
	 * @param t
	 */
	void delete(T t);

	/**
	 * 使用主鍵刪除
	 * 
	 * @param k
	 */
	void deleteByPk(K k);

	/**
	 * 使用物件查詢
	 * 
	 * @param t
	 * @param orders
	 * @return
	 */
	List<T> query(T t, Order... orders);

	/**
	 * 取得該物件對應到的 table 全部資料
	 * 
	 * @param clazz
	 * @param orders
	 * @return
	 */
	List<T> queryAll(Order... orders);

	/**
	 * 使用 Map 查詢（Map 鍵值須對應到該物件）
	 * 
	 * @param dataMap
	 * @param orders
	 * @return
	 */
	List<T> queryByMap(Map<String, Object> dataMap, Order... orders);

	/**
	 * 使用主鍵查詢
	 * 
	 * @param k
	 * @return
	 */
	T queryByPk(K k);

	/**
	 * 儲存物件
	 * 
	 * @param t
	 * @return
	 */
	T save(T t);

	/**
	 * 更新物件
	 * 
	 * @param t
	 * @return
	 */
	T update(T t);
}
