package com.dineshrepo.opensource.batch.backend.dao;

import java.io.Serializable;

public class CommitPrimaryKey implements Serializable  {

	private String commitId;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((commitId == null) ? 0 : commitId.hashCode());
		result = prime * result + repoId;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommitPrimaryKey other = (CommitPrimaryKey) obj;
		if (commitId == null) {
			if (other.commitId != null)
				return false;
		} else if (!commitId.equals(other.commitId))
			return false;
		if (repoId != other.repoId)
			return false;
		return true;
	}
	private int repoId;
	public CommitPrimaryKey() {

	}
	public CommitPrimaryKey(String commitId, int repoId) {
		this.commitId = commitId;
		this.repoId = repoId;
	}
}
