package com.kb.dao.base.support;

/**
 * HQL 運算子
 * 
 * @author KB
 * @version 1.0
 */
public enum HqlOperator {
	BETWEEN("@k@ between @v0@ and @v1@", 2), EQ("@k@ = @v0@", 1), GE("@k@ >= @v0@", 1), GT("@k@ > @v0@", 1), IN(
			"@k@ in (@v0@)", null), IS_NOT_NULL("@k@ is not null", 0), IS_NULL(" @k@ is null", 0), LE("@k@ <= @v0@",
					1), LT("@k@ < @v0@", 1), NOT("@k@ != @v0@", 1), NOT_IN("@k@ not in (@v0@)", null);

	private String operatorTemplate;
	private Integer paramsSize;

	private HqlOperator(String operatorTemplate, Integer paramsSize) {
		this.operatorTemplate = operatorTemplate;
		this.paramsSize = paramsSize;
	}

	public String getOperatorTemplate() {
		return operatorTemplate;
	}

	public Integer getParamsSize() {
		return paramsSize;
	}
}
