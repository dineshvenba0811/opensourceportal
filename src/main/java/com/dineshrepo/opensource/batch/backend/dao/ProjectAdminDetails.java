package com.dineshrepo.opensource.batch.backend.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@IdClass(ProjectAdminPrimaryKey.class)
@Table(name = "ProjectAdminDetails")
public class ProjectAdminDetails {

	@Id
	@Column(name = "projectId")
	private int projectId;
	@Column(name = "adminUserId")
	private int adminUserId;
	public int getAdminUserId() {
		return adminUserId;
	}
	public void setAdminUserId(int adminUserId) {
		this.adminUserId = adminUserId;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	
}
