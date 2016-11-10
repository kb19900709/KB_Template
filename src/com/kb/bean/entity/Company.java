package com.kb.bean.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.kb.bean.base.BaseEntity;
import com.kb.system.constant.EnvConstant;
import com.kb.utils.annotation.IgnoreInvoke;

/**
 * The persistent class for the company database table.
 * 
 */
@Entity
@Table(name = "company")
public class Company extends BaseEntity implements Serializable {

	@IgnoreInvoke
	private static final long serialVersionUID = 1L;

	@Column(name = "company_capitalization", nullable = false)
	private Integer companyCapitalization;

	@Id
	@GeneratedValue(generator = EnvConstant.HIBERNATE_ID_GENERATOR)
	@GenericGenerator(name = EnvConstant.HIBERNATE_ID_GENERATOR, strategy = EnvConstant.HIBERNATE_ID_GENERATOR_STRATEGY)
	@Column(name = "company_id", unique = true, nullable = false, length = 50)
	private String companyId;

	@Column(name = "company_mail", nullable = false, length = 50)
	private String companyMail;

	@Column(name = "company_name", nullable = false, length = 20)
	private String companyName;

	public Company() {
	}

	public Integer getCompanyCapitalization() {
		return this.companyCapitalization;
	}

	public String getCompanyId() {
		return this.companyId;
	}

	public String getCompanyMail() {
		return this.companyMail;
	}

	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyCapitalization(Integer companyCapitalization) {
		this.companyCapitalization = companyCapitalization;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public void setCompanyMail(String companyMail) {
		this.companyMail = companyMail;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

}