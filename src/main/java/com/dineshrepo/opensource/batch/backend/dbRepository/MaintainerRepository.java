package com.dineshrepo.opensource.batch.backend.dbRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dineshrepo.opensource.batch.backend.dao.MaintainerDetails;

/** repository for the contributor details table.
 * @author DCH2KOR
 *
 */
@Repository("MaintainerRepository")
public interface MaintainerRepository extends JpaRepository<MaintainerDetails, String>{

}
