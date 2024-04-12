package com.dineshrepo.opensource.batch.backend.dao;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

/** table stores the repo details for all opensource projects.
 * @author DCH2KOR
 *
 */
@Data
@Entity
@Table(name = "RepoDetails")
public class RepoDetails {

	@Id
	@Column(name = "repoId")
	private int repoId;

	@Column(name = "repoName")
	private String repoName;

	@Column(name = "state")
	private String state;

	
	@Column(name = "projectId")
	private int projectId;

	@Column(name = "projectKey")
	private String projectKey;
	
	@Column(name = "commitCount")
	private String commitCount;
	
	
	@Column(name = "firstCommitTime")
	private Date firstCommitTime;
	
	public Date getFirstCommitTime() {
		return firstCommitTime;
	}

	public void setFirstCommitTime(Date firstCommitTime) {
		this.firstCommitTime = firstCommitTime;
	}

	@Column(name = "latestCommitTime")
	private Date latestCommitTime;
	
	@Column(name = "description",length=5000)
	private String description;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getLatestCommitTime() {
		return latestCommitTime;
	}

	public void setLatestCommitTime(Date latestCommitTime) {
		this.latestCommitTime = latestCommitTime;
	}

	public String getCommitCount() {
		return commitCount;
	}

	public void setCommitCount(String commitCount) {
		this.commitCount = commitCount;
	}

	public String getForkCount() {
		return forkCount;
	}

	public void setForkCount(String forkCount) {
		this.forkCount = forkCount;
	}

	@Column(name = "forkCount")
	private String forkCount;


	public String getProjectKey() {
		return projectKey;
	}

	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public int getRepoId() {
		return repoId;
	}

	public void setRepoId(int repoId) {
		this.repoId = repoId;
	}


	public String getRepoName() {
		return repoName;
	}

	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
