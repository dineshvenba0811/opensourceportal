package com.dineshrepo.opensource.batch.backend.dbRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dineshrepo.opensource.batch.backend.dao.CommitterDetails;
@Repository("CommiterRepo")
public interface CommiterRepo extends JpaRepository<CommitterDetails, String>{
	
	/*
	 * @Query(
	 * value="SELECT u FROM CommitterDetails u WHERE u.repoName = :repoName ORDER BY u.latestCommitTime desc "
	 * ) CommitterDetails findLatestCommitId(@Param("repoName") String repoName);
	 */
}
