package com.dineshrepo.opensource.batch.backend.dao;

import java.io.Serializable;

public class MaintainerPrimaryKey implements Serializable  {

	private int maintainerId;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + maintainerId;
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
		MaintainerPrimaryKey other = (MaintainerPrimaryKey) obj;
		if (maintainerId != other.maintainerId)
			return false;
		if (repoId != other.repoId)
			return false;
		return true;
	}
	private int repoId;
	public MaintainerPrimaryKey() {

	}
	public MaintainerPrimaryKey(int maintainerId, int repoId) {
		this.maintainerId = maintainerId;
		this.repoId = repoId;
	}
	
}
