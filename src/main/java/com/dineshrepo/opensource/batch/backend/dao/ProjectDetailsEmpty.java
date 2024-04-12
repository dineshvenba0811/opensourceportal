package com.dineshrepo.opensource.batch.backend.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "ProjectDetailsEmpty")
public class ProjectDetailsEmpty {

	@Id
	@Column(name = "projectId")
	private int projectId;
	private String projectKey;
	private String projectName;
	private String bitBucketUrl;
	public String getBitBucketUrl() {
		return bitBucketUrl;
	}

	public void setBitBucketUrl(String bitBucketUrl) {
		this.bitBucketUrl = bitBucketUrl;
	}

	public String getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	private String projectDescription;
	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectKey() {
		return projectKey;
	}

	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}

	
}
