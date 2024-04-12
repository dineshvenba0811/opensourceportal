package com.bosch.opensource.batch.backend;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bosch.opensource.batch.backend.dao.CategoriesDetails;
import com.bosch.opensource.batch.backend.dao.CommitterDetails;
import com.bosch.opensource.batch.backend.dao.FileDetails;
import com.bosch.opensource.batch.backend.dao.MaintainerDetails;
import com.bosch.opensource.batch.backend.dao.ProjectAdminDetails;
import com.bosch.opensource.batch.backend.dao.ProjectDetailsTwo;
import com.bosch.opensource.batch.backend.dao.RepoDetails;
import com.bosch.opensource.batch.backend.dao.UserDetails;
import com.bosch.opensource.batch.backend.dbRepository.AdminDetailRepository;
import com.bosch.opensource.batch.backend.dbRepository.CategoryDetailRepository;
import com.bosch.opensource.batch.backend.dbRepository.CommiterRepo;
import com.bosch.opensource.batch.backend.dbRepository.FileDetailsRepository;
import com.bosch.opensource.batch.backend.dbRepository.MaintainerRepository;
import com.bosch.opensource.batch.backend.dbRepository.ProjectDetailsEmptyRepo;
import com.bosch.opensource.batch.backend.dbRepository.ProjectDetailsRepositoryInterface;
import com.bosch.opensource.batch.backend.dbRepository.RepoDetailsRepository;
import com.bosch.opensource.batch.backend.dbRepository.UserDetailsRepository;
import com.bosch.opensource.batch.backend.service.GetBitBucketApiConnection;
import com.bosch.opensource.batch.backend.service.GetCategoryDetails;
import com.bosch.opensource.batch.backend.service.GetCommitDetails;
import com.bosch.opensource.batch.backend.service.GetContributersForProjectRepo;
import com.bosch.opensource.batch.backend.service.GetFileDetails;
import com.bosch.opensource.batch.backend.service.GetNoOfForksRepos;
import com.bosch.opensource.batch.backend.service.GetProjectPermission;
import com.bosch.opensource.batch.backend.service.GetProjects;
import com.bosch.opensource.batch.backend.service.GetReposForProjects;
import com.bosch.opensource.batch.backend.service.GetUsers;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner {
	@Autowired
	GetBitBucketApiConnection apiCon;
	@Autowired 
	ProjectDetailsRepositoryInterface projects;
	@Autowired
	GetProjects projectService;
	@Autowired
	GetReposForProjects repoService;
	@Autowired
	RepoDetailsRepository repo;
	@Autowired
	GetProjectPermission projectPermission;
	@Autowired
	GetContributersForProjectRepo repoContributors;
	@Autowired
	MaintainerRepository maintainerRepo;
	@Autowired
	GetCommitDetails commitDetails;
	@Autowired
	GetNoOfForksRepos forkDetails;
	@Autowired
	GetCategoryDetails projectCategories;
	@Autowired
	CategoryDetailRepository categoryRepo;
	@Autowired
	GetFileDetails details;
	@Autowired
	FileDetailsRepository filedetailsRepo;
	@Autowired
	AdminDetailRepository adminrepo;
	@Autowired
	CommiterRepo commitRepo;
	@Autowired
	ProjectDetailsEmptyRepo projectSecondary;
	@Autowired
	GetUsers userdetails;
	@Autowired
	UserDetailsRepository userDetailRepo;
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		getUsers();
		getProjectDetails();
		getProjectRepoDetails();
		getCommitsNewRepos();
		getRepoMaintainers();
		getFileDetails() ;
	}
	private void getUsers() {
		List<UserDetails> userDetailsList=userdetails.getAllUsers();
		userDetailRepo.saveAll(userDetailsList);
	}
	/** getProjectDetails method get all the project from the api and check if its a opensource and save in the table.
	 *  table ProjectDetailsTwo
	 */
	private void getProjectDetails() {
		List<ProjectDetailsTwo> projectListfromApi= null;
		List<ProjectDetailsTwo> projectListfromApiopensource= null;
		try {
			projectListfromApi = projectService.getProjects();
			projectListfromApiopensource = getProjectPermissionandUpdate(projectListfromApi);
			getProjectAdminAndUpdate(projectListfromApiopensource);
			getCategories(projectListfromApiopensource);
			projects.saveAll(projectListfromApiopensource); 
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	/** getProjectPermissionandUpdate method gets the permission for the project
	 * @param projectList
	 * @return opensourceProjectList
	 */
	private List<ProjectDetailsTwo> getProjectPermissionandUpdate(List<ProjectDetailsTwo> projectList) {
		List<ProjectDetailsTwo> opensourceProjectList = new ArrayList<>();
		for(int listIndex=0;listIndex <projectList.size();listIndex ++) {
			boolean resultFlag=projectPermission.getPermissionDetailsforProjects(projectList.get(listIndex).getProjectKey(),
					projectList.get(listIndex).getProjectId(),projectList.get(listIndex).getProjectName(),
					projectList.get(listIndex).getBitBucketUrl(),
					projectList.get(listIndex).getProjectDescription());
			if(resultFlag) {
				ProjectDetailsTwo opensourceProjectBean=new ProjectDetailsTwo();
				opensourceProjectBean.setProjectKey(projectList.get(listIndex).getProjectKey());
				opensourceProjectBean.setBitBucketUrl(projectList.get(listIndex).getBitBucketUrl());
				opensourceProjectBean.setProjectDescription(projectList.get(listIndex).getProjectDescription());
				opensourceProjectBean.setProjectId(projectList.get(listIndex).getProjectId());
				opensourceProjectBean.setProjectName(projectList.get(listIndex).getProjectName());
				opensourceProjectList.add(opensourceProjectBean);
			}
		}
		return opensourceProjectList;
	}
	/** getProjectAdminAndUpdate method fetch admin of the projects and insert in the table 
	 * table name:ProjectAdminDetails
	 */
	private void getProjectAdminAndUpdate(List<ProjectDetailsTwo> newopensourceProjectList) {
		for(int i=0;i<newopensourceProjectList.size();i++) {
			List<ProjectAdminDetails> newprojectAdminList=projectPermission.getAdminforProjects(newopensourceProjectList.get(i).getProjectKey(),
					newopensourceProjectList.get(i).getProjectId());
			adminrepo.saveAll(newprojectAdminList);
		} 
	}
	/** getCategories method extract category for the project from the api and insert into the  table.
	 * table name :CategoriesDetails
	 */
	private void getCategories(List<ProjectDetailsTwo> projectListfromApi ) {
		for(int index=0;index<projectListfromApi.size();index ++) {
			List<CategoriesDetails> categoryBeanList =projectCategories.getCategory(projectListfromApi.get(index).getProjectKey());
			categoryRepo.saveAll(categoryBeanList);
		}
		List<ProjectDetailsTwo> categoryopensourceProjects=projectService.checkWithCategory();
		projects.saveAll(categoryopensourceProjects);
	}
	private void getProjectRepoDetails() {
		List<ProjectDetailsTwo> projectListfromApi=projects.findAll();
		for(int listIndex=0;listIndex <projectListfromApi.size();listIndex ++) {
			List<RepoDetails> newProjectRepoDetailsList=repoService.getRepoNames(projectListfromApi.get(listIndex).getProjectKey());
			repo.saveAll(newProjectRepoDetailsList);
		}
	} 
	private void getCommitsNewRepos() {
		List <RepoDetails> repoList=repo.findAll();
		for(int repoIndex=0;repoIndex < repoList.size(); repoIndex++) {
			List<CommitterDetails> repoCommitList=repoContributors.getListOfCommittersForProjectRepos(repoList.get(repoIndex).getProjectKey(),
					repoList.get(repoIndex).getRepoName(),repoList.get(repoIndex).getRepoId());	
			commitRepo.saveAll(repoCommitList);
		}
	}

	private void getRepoMaintainers() {
		List <RepoDetails> repoList=repo.findAll();
		for(int listIndex=0;listIndex <repoList.size();listIndex ++) {
			List<MaintainerDetails> repoParticipantList=repoContributors.getListOfMaintainersForProjectRepos
					(repoList.get(listIndex).getProjectKey(), repoList.get(listIndex).getRepoName(),
							repoList.get(listIndex).getRepoId());
			maintainerRepo.saveAll(repoParticipantList);
		}
	}

	private void getFileDetails() {
		List <RepoDetails> repoList=repo.findAll();
		List<FileDetails> repoFilesList=new ArrayList<>();
		for(int listIndex=0;listIndex <repoList.size();listIndex ++) {
			List<FileDetails> fileList=details.getFilesOftheRepo(repoList.get(listIndex).getProjectKey(),
					repoList.get(listIndex).getRepoName());
			for(int j=0;j<fileList.size();j++) {
				FileDetails bean=new FileDetails();
				int fileloc=details.getFileDetails(fileList.get(j).getFileName(),repoList.get(listIndex).getProjectKey(),
						repoList.get(listIndex).getRepoName());
				String fileName=fileList.get(j).getFileName();
				int lastindex=fileName.lastIndexOf(".");
				bean.setLoc(String.valueOf(fileloc));
				bean.setProjectKey(repoList.get(listIndex).getProjectKey());
				bean.setRepoName(repoList.get(listIndex).getRepoName());
				bean.setFileName(fileList.get(j).getFileName());
				bean.setFileExtension(fileName.substring(lastindex+1));
				repoFilesList.add(bean);
			}
			filedetailsRepo.saveAll(repoFilesList);
		}
	}
}
