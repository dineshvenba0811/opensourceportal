package com.dineshrepo.opensource.batch.backend.dao;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
@IdClass(CommitPrimaryKey.class)
@Table(name = "CommitterDetails")
public class CommitterDetails {
	@Id
	@Size(min = 3, max = 65)
	@Column(name="commitId",length = 50)
	private String commitId;
	@Column(name="repoId")
	private int repoId;
	@Column(name="committerId")
	private int committerId;
	@Column(name="timestamp")
	private Date timestamp;
	public String getCommitId() {
		return commitId;
	}
	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}
	public int getRepoId() {
		return repoId;
	}
	public void setRepoId(int repoId) {
		this.repoId = repoId;
	}
	public int getCommitterId() {
		return committerId;
	}
	public void setCommitterId(int committerId) {
		this.committerId = committerId;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
