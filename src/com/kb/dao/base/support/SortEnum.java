package com.kb.dao.base.support;

/**
 * HQL Sort 列舉
 * 
 * @author KB
 * @version 1.0
 */
public enum SortEnum {
	ASC("asc"), DESC("desc");

	private String sort;

	private SortEnum(String sort) {
		this.sort = sort;
	}

	public String getSort() {
		return sort;
	}
}
