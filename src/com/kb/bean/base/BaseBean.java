package com.kb.bean.base;

import com.kb.utils.DeveloperUtils;

/**
 * Bean 基礎行為定義
 * 
 * @author KB
 * @version 1.0
 */
public class BaseBean {
	@Override
	public String toString() {
		return DeveloperUtils.beanToString(this);
	}
}
