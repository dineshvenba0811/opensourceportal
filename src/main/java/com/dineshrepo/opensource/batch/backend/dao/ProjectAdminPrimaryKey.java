package com.dineshrepo.opensource.batch.backend.dao;

import java.io.Serializable;

public class ProjectAdminPrimaryKey implements Serializable{

	private int adminUserId;
	private int projectId;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + adminUserId;
		result = prime * result + projectId;
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
		ProjectAdminPrimaryKey other = (ProjectAdminPrimaryKey) obj;
		if (adminUserId != other.adminUserId)
			return false;
		if (projectId != other.projectId)
			return false;
		return true;
	}
	public ProjectAdminPrimaryKey() {

	}
	public ProjectAdminPrimaryKey(int adminUserId, int projectId) {
		this.adminUserId = adminUserId;
		this.projectId = projectId;
	}
}
