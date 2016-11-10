package com.kb.system.web.hibernate;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import com.kb.system.constant.EnvConstant;
import com.kb.system.web.thread.ThreadLocalHandler;
import com.kb.system.web.thread.bean.ThreadLog;
import com.kb.utils.DeveloperUtils;

/**
 * HibernateDao 執行 data access 時會觸發的 HibernateInterceptor，將自動處理 who columns
 * 
 * @author KB
 * @version 1.0
 * @see com.kb.bean.base.BaseEntity
 */
public class HibernateInterceptor extends EmptyInterceptor {

	private static final String DELETE = "delete";
	private static final String SAVE = "save";
	private static final long serialVersionUID = 1L;
	private static final String UPDATE = "update";

	@Override
	public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		entity = initWhoColumns(entity, UPDATE);
		return refactorState(entity, currentState, propertyNames);
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		entity = initWhoColumns(entity, SAVE);
		return refactorState(entity, state, propertyNames);
	}

	/**
	 * 
	 * @param entity
	 *            目標物件
	 * @param action
	 *            執行常數
	 * @return entity
	 */
	private Object initWhoColumns(Object entity, String action) {
		ThreadLog threadLog = ThreadLocalHandler.get();
		String userName = threadLog.getUserName();

		switch (action) {
		case SAVE:
			// 更新建立者名稱，修改者名稱
			processEntity(entity,
					new String[] { EnvConstant.ENTITY_CREATED_USER_FIELD, EnvConstant.ENTITY_MODIFY_USER_FIELD },
					userName);
			// 更新建立日期，修改日期
			processEntity(entity,
					new String[] { EnvConstant.ENTITY_CREATED_DATE_FIELD, EnvConstant.ENTITY_MODIFY_DATE_FIELD },
					new Date());
			break;
		case UPDATE:
			// 更新修改者名稱
			processEntity(entity, new String[] { EnvConstant.ENTITY_MODIFY_USER_FIELD }, userName);
			// 更新修改日期
			processEntity(entity, new String[] { EnvConstant.ENTITY_MODIFY_DATE_FIELD }, new Date());
			break;
		case DELETE:
			break;
		}

		return entity;
	}

	/**
	 * 
	 * @param entity
	 *            目標物件
	 * @param updateFieldNames
	 *            目標屬性陣列
	 * @param updataValue
	 *            更改的物件
	 * @see com.kb.utils.DeveloperUtils
	 */
	private void processEntity(Object entity, String[] updateFieldNames, Object updataValue) {
		for (String fieldName : updateFieldNames) {
			DeveloperUtils.invokeBeanMethod(entity, fieldName, updataValue);
		}
	}

	/**
	 * 更改 EmptyInterceptor 傳入參數 state 陣列中的值
	 * 
	 * @param entity
	 * @param state
	 * @param propertyNames
	 */
	private boolean refactorState(Object entity, Object[] state, String[] propertyNames) {
		String fieldName;
		for (int i = 0; i < propertyNames.length; i++) {
			fieldName = propertyNames[i];
			state[i] = DeveloperUtils.invokeBeanMethod(entity, fieldName);
		}
		return true;
	}
}
