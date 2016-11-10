package com.kb.dao.base.support;

/**
 * 排序列舉
 * 
 * @author KB
 * @version 1.0
 */
public class Order {

	/**
	 * 
	 * @param fieldName
	 *            產生排序Entity的屬性名稱
	 * @param orderSup
	 *            {@link com.kb.dao.base.support.SortEnum}
	 */
	public static Order asOrder(String fieldName, SortEnum orderSup) {
		return new Order(fieldName, orderSup);
	}

	private String fieldName;

	private SortEnum orderSup;

	private Order(String fieldName, SortEnum orderSup) {
		this.fieldName = fieldName;
		this.orderSup = orderSup;
	}

	public String getFieldName() {
		return fieldName;
	}

	public SortEnum getOrderSup() {
		return orderSup;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public void setOrderSup(SortEnum orderSup) {
		this.orderSup = orderSup;
	}
}
