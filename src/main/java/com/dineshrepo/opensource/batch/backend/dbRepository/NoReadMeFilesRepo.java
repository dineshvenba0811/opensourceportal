package com.dineshrepo.opensource.batch.backend.dbRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dineshrepo.opensource.batch.backend.dao.NoReadMeFiles;
@Repository("NoReadMeFilesRepo")
public interface NoReadMeFilesRepo  extends JpaRepository<NoReadMeFiles, String>{

}
