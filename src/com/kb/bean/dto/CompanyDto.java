package com.kb.bean.dto;

import java.math.BigDecimal;

import com.kb.bean.base.BaseDto;

public class CompanyDto extends BaseDto {
	private BigDecimal avgCap;
	private Integer total;

	public BigDecimal getAvgCap() {
		return avgCap;
	}

	public Integer getTotal() {
		return total;
	}

	public void setAvgCap(BigDecimal avgCap) {
		this.avgCap = avgCap;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

}
