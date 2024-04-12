package com.dineshrepo.opensource.batch.backend.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "MaintainerDetailsEmpty")
public class MaintainerDetailsEmpty {
	@Id
	@Column(name = "repoId")
	private int repoId;

	public int getRepoId() {
		return repoId;
	}

	public void setRepoId(int repoId) {
		this.repoId = repoId;
	}
}
