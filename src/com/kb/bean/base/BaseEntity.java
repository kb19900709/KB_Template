package com.kb.bean.base;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.kb.system.constant.EnvConstant;

/**
 * 常態屬性 (who columns)
 * 
 * @author KB
 * @version 1.0
 */
@MappedSuperclass
public class BaseEntity extends BaseBean {

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = EnvConstant.ENTITY_CREATED_DATE, nullable = false)
	private Date createdDate;

	@Column(name = EnvConstant.ENTITY_CREATED_USER, nullable = false, length = 10)
	private String createdUser;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = EnvConstant.ENTITY_MODIFY_DATE, nullable = false)
	private Date modifyDate;

	@Column(name = EnvConstant.ENTITY_MODIFY_USER, nullable = false, length = 10)
	private String modifyUser;

	public Date getCreatedDate() {
		return createdDate;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
}
