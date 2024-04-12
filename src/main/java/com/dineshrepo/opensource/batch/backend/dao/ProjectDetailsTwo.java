package com.dineshrepo.opensource.batch.backend.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "ProjectDetailsTwo")
public class ProjectDetailsTwo {
	@Id
	@Column(name = "projectId")
	private int projectId;
	@Column(name = "projectKey")
	private String projectKey;
	@Column(name = "projectName")
	private String projectName;
	@Column(name = "projectDescription")
	private String projectDescription;
	@Column(name = "projecturl")
	private String projecturl;
	@Column(name = "bitBucketUrl")
	private String bitBucketUrl;
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	
	public String getProjectKey() {
		return projectKey;
	}
	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectDescription() {
		return projectDescription;
	}
	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}
	public String getProjecturl() {
		return projecturl;
	}
	public void setProjecturl(String projecturl) {
		this.projecturl = projecturl;
	}
	public String getBitBucketUrl() {
		return bitBucketUrl;
	}
	public void setBitBucketUrl(String bitBucketUrl) {
		this.bitBucketUrl = bitBucketUrl;
	}




}
