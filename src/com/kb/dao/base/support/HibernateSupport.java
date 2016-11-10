package com.kb.dao.base.support;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.kb.bean.base.BaseDto;
import com.kb.bean.base.BaseEntity;
import com.kb.exception.dao.DtoBindingException;
import com.kb.exception.dao.HqlFunctionException;
import com.kb.utils.DeveloperUtils;
import com.kb.utils.date.DateFormat;

/**
 * HqlFunction 提供者
 * 
 * @author KB
 * @version 1.0
 */
@Component
public class HibernateSupport {

	public static class HqlFunction {
		private Map<String, Integer> countField = new HashMap<>();
		private List<String> hqlFunctionActualRecord = new ArrayList<>();
		private List<String> hqlFunctionRecord = new ArrayList<>();
		private Map<String, Object> queryMap = new HashMap<>();
		private Class<?> targetClass;

		/**
		 * 初始化屬性計數 map
		 * 
		 * @param targetClass
		 */
		private HqlFunction(Class<?> targetClass) {
			this.targetClass = targetClass;
			for (Field field : targetClass.getDeclaredFields()) {
				countField.put(field.getName(), 0);
			}
			for (Field field : targetClass.getSuperclass().getDeclaredFields()) {
				countField.put(field.getName(), 0);
			}
		}

		/**
		 * For IN or NOT_IN mode (and)
		 * 
		 * @param fieldName
		 *            物件屬性名稱
		 * @param hqlOperator
		 *            HQL function
		 * @param valueArray
		 *            參數參考
		 * @return
		 * @see com.kb.dao.base.support.HqlOperator
		 */
		public HqlFunction addCondition(String fieldName, HqlOperator hqlOperator,
				Collection<? extends Serializable> valueArray) {
			return addCondition(fieldName, hqlOperator, getSerializableArray(valueArray));
		}

		/**
		 * For normal HqlOperator type (and)
		 * 
		 * @param fieldName
		 *            物件屬性名稱
		 * @param hqlOperator
		 *            HQL function
		 * @param valueArray
		 *            參數參考
		 * @return
		 * @see com.kb.dao.base.support.HqlOperator
		 */
		@SuppressWarnings("unchecked")
		public <V extends Serializable> HqlFunction addCondition(String fieldName, HqlOperator hqlOperator,
				V... valueArray) {
			List<String> hqlFunctionResult = processHqlFunctionGenerator(fieldName, hqlOperator, valueArray);
			if (CollectionUtils.isNotEmpty(hqlFunctionResult) && hqlFunctionResult.size() == 2) {
				hqlFunctionRecord.add(hqlFunctionResult.get(0));
				hqlFunctionActualRecord.add(hqlFunctionResult.get(1));
				return this;
			}
			throw new HqlFunctionException(
					"HqlFunction.processHqlFunctionGenerator transfer hql internal error... please check!!!");
		}

		/**
		 * 取得 HQL 實際紀錄
		 * 
		 * @return
		 */
		public List<String> getHqlFunctionActualRecord() {
			return hqlFunctionActualRecord;
		}

		/**
		 * 取得 HQL
		 * 
		 * @return
		 */
		public List<String> getHqlFunctionRecord() {
			return hqlFunctionRecord;
		}

		/**
		 * 取得 QueryMap
		 * 
		 * @return
		 */
		public Map<String, Object> getQueryMap() {
			return queryMap;
		}

		/**
		 * For IN or NOT_IN mode (or)
		 * 
		 * @param fieldName
		 *            物件屬性名稱
		 * @param hqlOperator
		 *            HQL function
		 * @param valueArray
		 *            參數參考
		 * @return
		 * @see com.kb.dao.base.support.HqlOperator
		 */
		public <V extends Serializable> HqlFunction orPreCondition(String fieldName, HqlOperator hqlOperator,
				Collection<? extends Serializable> valueArray) {
			return orPreCondition(fieldName, hqlOperator, getSerializableArray(valueArray));
		}

		/**
		 * 使用 or 運算子連接前一個 function 表達式 (or)
		 * 
		 * @param fieldName
		 *            物件屬性名稱
		 * @param hqlOperator
		 *            HQL function
		 * @param valueArray
		 *            參數參考
		 * @return
		 * @see com.kb.dao.base.support.HqlOperator
		 */
		@SuppressWarnings("unchecked")
		public <V extends Serializable> HqlFunction orPreCondition(String fieldName, HqlOperator hqlOperator,
				V... valueArray) {
			validateHqlFunctionRecord();
			List<String> hqlFunctionResult = processHqlFunctionGenerator(fieldName, hqlOperator, valueArray);
			if (CollectionUtils.isNotEmpty(hqlFunctionResult) && hqlFunctionResult.size() == 2) {
				formatOrCondition(hqlFunctionRecord, hqlFunctionResult.get(0));
				formatOrCondition(hqlFunctionActualRecord, hqlFunctionResult.get(1));
				return this;
			}
			throw new HqlFunctionException(
					"HqlFunction.processHqlFunctionGenerator transfer hql internal error... please check!!!");
		}

		/**
		 * 轉換 or 格式
		 * 
		 * @param hqlRecord
		 * @param newCondition
		 */
		private void formatOrCondition(List<String> hqlRecord, String newCondition) {
			int lastElementIndex = hqlRecord.size() - 1;
			String lastElement = hqlRecord.get(lastElementIndex);
			hqlRecord.remove(lastElementIndex);
			hqlRecord.add(String.format("( %s or %s )", lastElement, newCondition));
		}

		/**
		 * 取得 Serializable array
		 * 
		 * @param valueArray
		 * @return
		 */
		private Serializable[] getSerializableArray(Collection<? extends Serializable> valueArray) {
			if (CollectionUtils.isEmpty(valueArray)) {
				throw new HqlFunctionException("HqlFunction.getSerializableArray valueArray is empty, please check");
			}
			Serializable[] serializableArray = new Serializable[valueArray.size()];
			serializableArray = valueArray.toArray(serializableArray);
			return serializableArray;
		}

		/**
		 * 產生 function HQL
		 * 
		 * @param fieldName
		 * @param hqlOperator
		 * @param valueArray
		 * @return
		 */
		@SuppressWarnings("unchecked")
		private <V extends Serializable> List<String> processHqlFunctionGenerator(String fieldName,
				HqlOperator hqlOperator, V... valueArray) {
			List<String> result = new ArrayList<>();

			if (StringUtils.isEmpty(fieldName) || hqlOperator == null) {
				throw new HqlFunctionException(
						"HqlFunction.processHqlFunctionGenerator params not illegal, please check!!!");
			}
			if (ArrayUtils.isNotEmpty(valueArray) && hqlOperator.getParamsSize() != null
					&& valueArray.length != hqlOperator.getParamsSize()) {
				throw new HqlFunctionException(String.format(
						"HqlFunction.processHqlFunctionGenerator hql template:%s ,need %s params. But actual:%s",
						hqlOperator.getOperatorTemplate(), hqlOperator.getParamsSize(), valueArray.length));
			}

			Integer count = countField.get(fieldName);
			if (count == null) {
				throw new HqlFunctionException(
						String.format("Filed name not found in %s:%s", targetClass.getSimpleName(), fieldName));
			}

			String hqlTemplate = hqlOperator.getOperatorTemplate().replaceAll("@k@",
					String.format("%s.%s", ALIAS, fieldName));
			String hqlActualRecord = new String(hqlTemplate);

			String key;
			Serializable value, cloneValue;

			if (hqlOperator.equals(HqlOperator.IN) || hqlOperator.equals(HqlOperator.NOT_IN)) {

				String arrayRecord = null, arrayActualRecord = null;
				for (int i = 0; i < valueArray.length; i++) {
					count = countField.get(fieldName);
					countField.put(fieldName, count += 1);

					key = String.format("%s_%s", fieldName, count.toString());
					value = valueArray[i];
					cloneValue = SerializationUtils.clone(value);

					if (cloneValue instanceof Date) {
						cloneValue = DeveloperUtils.timeFormat((Date) cloneValue, DateFormat.TIMESTAMP);
					}

					queryMap.put(key, value);

					if (i != 0) {
						arrayRecord += String.format(",:%s", key);
						arrayActualRecord += String.format(",'%s'", cloneValue.toString());
						continue;
					}
					arrayRecord = String.format(":%s", key);
					arrayActualRecord = String.format("'%s'", cloneValue.toString());
				}

				hqlTemplate = hqlTemplate.replace("@v0@", arrayRecord);
				hqlActualRecord = hqlActualRecord.replace("@v0@", arrayActualRecord);

				result.add(hqlTemplate);
				result.add(hqlActualRecord);
				return result;
			}

			for (int i = 0; i < hqlOperator.getParamsSize(); i++) {
				count = countField.get(fieldName);
				countField.put(fieldName, count += 1);

				key = String.format("%s_%s", fieldName, count.toString());
				value = valueArray[i];
				cloneValue = SerializationUtils.clone(value);

				if (cloneValue instanceof Date) {
					cloneValue = DeveloperUtils.timeFormat((Date) cloneValue, DateFormat.TIMESTAMP);
				}

				hqlTemplate = hqlTemplate.replace(String.format("@v%s@", i), String.format(":%s", key));
				hqlActualRecord = hqlActualRecord.replace(String.format("@v%s@", i),
						String.format("'%s'", cloneValue.toString()));
				queryMap.put(key, value);
			}

			result.add(hqlTemplate);
			result.add(hqlActualRecord);
			return result;
		}

		/**
		 * 驗證 HQL 集合是否有資料
		 */
		private void validateHqlFunctionRecord() {
			if (CollectionUtils.isEmpty(hqlFunctionRecord) || CollectionUtils.isEmpty(hqlFunctionActualRecord)) {
				throw new HqlFunctionException(
						"HqlFunction.validateHqlFunctionRecord hqlFunctionRecord or hqlFunctionActualRecord is empty ... please check!!!");
			}
		}
	}

	private static String ALIAS;

	public static <D extends BaseDto> void bindingDtoScalar(SQLQuery query, Class<D> clazz) {
		if (query == null || clazz == null) {
			throw new DtoBindingException("HibernateSupport.dtoBinding some param is null,please check!!!");
		}

		Type type;
		for (Field field : clazz.getDeclaredFields()) {
			type = getHibernateType(field.getType());
			if (type == null) {
				throw new DtoBindingException(
						String.format("HibernateSupport.dtoBinding field %s type %s is not support!!!", field.getName(),
								field.getType().getSimpleName()));
			}
			query.addScalar(field.getName(), type);
		}
	}

	/**
	 * 取得 HqlFunction 物件
	 * 
	 * @param targetClass
	 *            目標 Entity
	 * @return
	 */
	public static HqlFunction getNewHqlFunction(Class<? extends BaseEntity> targetClass) {
		if (targetClass == null) {
			throw new HqlFunctionException(
					"HibernateSupport.getNewHqlFunction HqlFunction initial error, please check construct params");
		}
		return new HqlFunction(targetClass);
	}

	private static Type getHibernateType(Class<?> fieldType) {
		if (fieldType.equals(String.class)) {
			return StringType.INSTANCE;
		}
		if (fieldType.equals(Integer.class)) {
			return IntegerType.INSTANCE;
		}
		if (fieldType.equals(Float.class)) {
			return FloatType.INSTANCE;
		}
		if (fieldType.equals(Double.class)) {
			return DoubleType.INSTANCE;
		}
		if (fieldType.equals(BigDecimal.class)) {
			return BigDecimalType.INSTANCE;
		}
		if (fieldType.equals(Date.class)) {
			return DateType.INSTANCE;
		}
		if (fieldType.equals(Boolean.class)) {
			return BooleanType.INSTANCE;
		}
		return null;
	}

	@Value("${env.hibernate.alias}")
	private void initAlias(String alias) {
		ALIAS = alias;
	}
}
