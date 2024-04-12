package com.dineshrepo.opensource.batch.backend.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

/** ContributorDetails table which stores data of thw contributors of the project repo's.
 * @author DCH2KOR
 *
 */
@Data
@Entity
@Table(name = "MaintainerDetails")
@IdClass(MaintainerPrimaryKey.class)
public class MaintainerDetails {

	@Id
	@Column(name = "maintainerId")
	private int maintainerId;
	public int getMaintainerId() {
		return maintainerId;
	}
	public void setMaintainerId(int maintainerId) {
		this.maintainerId = maintainerId;
	}
	@Id
	@Column(name = "repoId")
	private int repoId;
	private String permission;
	public int getRepoId() {
		return repoId;
	}
	public void setRepoId(int repoId) {
		this.repoId = repoId;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
}
