package com.dineshrepo.opensource.batch.backend.dbRepository;

import java.sql.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dineshrepo.opensource.batch.backend.dao.RepoDetails;

/** repository for the repo details table.
 * @author DCH2KOR
 *
 */
@Repository("RepoDetailsRepository")
public interface RepoDetailsRepository extends JpaRepository<RepoDetails, String>{

	@Modifying
	@Query("update RepoDetails as rd set rd.commitCount = :commitCount, rd.latestCommitTime= :latestCommitTime, "
			+ " rd.firstCommitTime= :firstCommitTime,  rd.forkCount= :forkCount  where  rd.repoId= :repoId ")
	@Transactional
	int updateCommitAndForkCount(@Param("commitCount") String commitCount,
			@Param("latestCommitTime") Date latestCommitTime,
			@Param("firstCommitTime") Date firstCommitTime,
			@Param("forkCount") String forkCount,
			@Param("repoId") int repoId);

}
