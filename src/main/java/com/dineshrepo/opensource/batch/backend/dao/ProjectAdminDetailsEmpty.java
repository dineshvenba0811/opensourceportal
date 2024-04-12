package com.dineshrepo.opensource.batch.backend.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "ProjectAdminDetailsEmpty")
public class ProjectAdminDetailsEmpty {

	@Id
	@Column(name = "projectId")
	private int projectId;

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
}
