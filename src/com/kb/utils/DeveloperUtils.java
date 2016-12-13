package com.kb.utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kb.exception.utils.DeveloperException;
import com.kb.system.constant.EnvConstant;
import com.kb.utils.annotation.IgnoreInvoke;
import com.kb.utils.date.DateFormat;

/**
 * 開發者補助工具類
 * 
 * @author KB
 * @version 1.0
 */
public class DeveloperUtils {

	private static Logger logger = LoggerFactory.getLogger(DeveloperUtils.class);

	private static final SimpleDateFormat SDF_NORMAL = new SimpleDateFormat(DateFormat.NORMAL_DATE.getTimeFormat());
	private static final SimpleDateFormat SDF_TIMESTAMP = new SimpleDateFormat(DateFormat.TIMESTAMP.getTimeFormat());

	/**
	 * Bean 轉換成 Map<String.String>，但是移除 class 屬性
	 * 
	 * @param obj
	 * @return
	 */
	public static Map<String, String> beanToMapMsg(Object obj) {
		Map<String, String> result = null;
		try {
			result = BeanUtils.describe(obj);
			result.remove("class");
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			String errorMsg = String.format("DeveloperUtils.beanToMapMsg transfer error at %s ... %s",
					obj.getClass().getSimpleName(), e.getMessage());
			logger.error(errorMsg);
			throw new DeveloperException(errorMsg);
		}
		return result;
	}

	/**
	 * 依據目標物件設定該物件的 Serializable Map ,會忽略被 IgnoreInvoke 註解標注的屬性
	 * 
	 * @param target
	 * @return
	 */
	public static Map<String, Serializable> beanToSerializableMap(Object target) {
		Map<String, Serializable> beanFiledAndValue = new HashMap<>();
		Class<?> targetClass = target.getClass();
		settingSerializableMap(target, beanFiledAndValue, targetClass.getDeclaredFields());
		if (targetClass.getSuperclass() != null) {
			settingSerializableMap(target, beanFiledAndValue, targetClass.getSuperclass().getDeclaredFields());
		}
		return beanFiledAndValue;
	}

	/**
	 * 印出 Bean 的屬性以及值
	 * 
	 * @param obj
	 * @return
	 */
	public static String beanToString(Object obj) {
		Map<String, String> result = beanToMapMsg(obj);
		if (MapUtils.isNotEmpty(result)) {
			return result.toString();
		}
		return null;
	}

	/**
	 * 取得方法名稱
	 * 
	 * @param prefix
	 * @param fieldName
	 * @return
	 */
	public static String getBeanMethodName(String prefix, String fieldName) {
		if (StringUtils.isEmpty(prefix) || StringUtils.isEmpty(fieldName)) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(prefix);
		sb.append(fieldName.toUpperCase().charAt(0) + fieldName.substring(1));
		return sb.toString();
	}

	/**
	 * 取得泛型類別
	 * 
	 * @param target
	 * @param index
	 * @return
	 */
	public static Class<?> getGenericClass(Object target, int index) {
		return (Class<?>) ((ParameterizedType) target.getClass().getGenericSuperclass())
				.getActualTypeArguments()[index];
	}

	/**
	 * java bean invoke getter/setter support
	 * 
	 * @param bean
	 *            對象物件
	 * @param fieldName
	 *            對象物件屬性名稱
	 * @param invokeObj
	 *            如果有值，設定為 setter method invoke
	 * @return
	 */
	public static Object invokeBeanMethod(Object bean, String fieldName, Object... invokeObj) {
		Class<?> clazz = bean.getClass();
		Method method;
		String methodName;
		if (ArrayUtils.isEmpty(invokeObj)) {
			methodName = getBeanMethodName(EnvConstant.BEAN_GET_PREFIX, fieldName);
			try {
				method = clazz.getMethod(methodName);
			} catch (NoSuchMethodException | SecurityException e) {
				String errorMsg = String.format("DeveloperUtils.invokeMethod get getter method error ... %s",
						e.getMessage());
				logger.error(errorMsg);
				throw new DeveloperException(errorMsg);
			}

			try {
				return method.invoke(bean);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				String errorMsg = String.format("DeveloperUtils.invokeMethod invoke getter method error ... %s",
						e.getMessage());
				logger.error(errorMsg);
				throw new DeveloperException(errorMsg);
			}
		}

		if (invokeObj.length > 1) {
			logger.warn("DeveloperUtils.invokeBeanMethod params invokeObj array's length more then one"
					+ ", the other value will ignore and it's not promise process will success!!!");
		}

		Object invokeObject = invokeObj[0];
		methodName = getBeanMethodName(EnvConstant.BEAN_SET_PREFIX, fieldName);
		try {
			method = clazz.getMethod(methodName, invokeObject.getClass());
		} catch (NoSuchMethodException | SecurityException e) {
			String errorMsg = String.format("DeveloperUtils.invokeMethod get setter method error ... %s",
					e.getMessage());
			logger.error(errorMsg);
			throw new DeveloperException(errorMsg);
		}

		try {
			return method.invoke(bean, invokeObject);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			String errorMsg = String.format("DeveloperUtils.invokeMethod invoke setter method error ... %s",
					e.getMessage());
			logger.error(errorMsg);
			throw new DeveloperException(errorMsg);
		}
	}

	/**
	 * Map<String,Object> 轉換成 bean
	 * 
	 * @param dataMap
	 *            properties
	 * @param clazz
	 *            target class
	 * @return
	 */
	public static <T> T mapToBean(Map<String, Object> dataMap, Class<T> clazz) {
		T result = null;
		try {
			result = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			String errorMsg = String.format("DeveloperUtils.mapToBean error when the class %s newInstance ... %s ",
					clazz.getSimpleName(), e.getMessage());
			logger.error(errorMsg);
			throw new DeveloperException(errorMsg);
		}
		try {
			BeanUtils.populate(result, dataMap);
		} catch (IllegalAccessException | InvocationTargetException e) {
			String errorMsg = String.format(
					"DeveloperUtils.mapToBean error when copy properties dataMap:%s to bean:%s ... %s", dataMap,
					clazz.getSimpleName(), e.getMessage());
			logger.error(errorMsg);
			throw new DeveloperException(errorMsg);
		}
		return result;
	}

	/**
	 * 取得時間 format
	 * 
	 * @param date
	 * @param dateFormat
	 * @return
	 * @see com.kb.utils.date.DateFormat
	 */
	public static String timeFormat(Date date, DateFormat dateFormat) {
		if (date == null || dateFormat == null) {
			String errorMsg = "DeveloperUtils.timeFormat error,some parameter null and need to be checked";
			logger.error(errorMsg);
			throw new DeveloperException(errorMsg);
		}

		switch (dateFormat) {
		case NORMAL_DATE:
			return SDF_NORMAL.format(date);
		case TIMESTAMP:
			return SDF_TIMESTAMP.format(date);
		default:
			return null;
		}
	}

	/**
	 * 忽略被 IgnoreInvoke 標注的屬性
	 * 
	 * @param obj
	 * @param serializableMap
	 * @param fieldArray
	 * @see com.kb.utils.annotation.IgnoreInvoke
	 */
	private static void settingSerializableMap(Object obj, Map<String, Serializable> serializableMap,
			Field[] fieldArray) {
		for (Field field : fieldArray) {
			if (field.getType() instanceof Serializable && field.getAnnotation(IgnoreInvoke.class) == null) {
				serializableMap.put(field.getName(), (Serializable) invokeBeanMethod(obj, field.getName()));
			}
		}
	}
}