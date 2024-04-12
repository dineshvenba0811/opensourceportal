package com.dineshrepo.opensource.batch.backend.dbRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dineshrepo.opensource.batch.backend.dao.ProjectDetailsTwo;

/** repository for the project details table.
 * @author DCH2KOR
 *
 */
@Repository("ProjectDetailsRepositoryInterface")
public interface ProjectDetailsRepositoryInterface extends JpaRepository<ProjectDetailsTwo, String>{

	
	
	
}
