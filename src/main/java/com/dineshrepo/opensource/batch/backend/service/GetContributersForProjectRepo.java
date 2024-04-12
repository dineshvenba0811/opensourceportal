package com.bosch.opensource.batch.backend.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bosch.opensource.batch.backend.dao.CommitterDetails;
import com.bosch.opensource.batch.backend.dao.MaintainerDetails;
import com.bosch.opensource.batch.backend.dao.MaintainerDetailsEmpty;
import com.bosch.opensource.batch.backend.dao.RepoDetails;
import com.bosch.opensource.batch.backend.dbRepository.MaintainerDetailsEmptyRepo;
import com.bosch.opensource.batch.backend.dbRepository.RepoDetailsRepository;

/** contributor details for the project
 * @param projectname , reponame.
 * @author DCH2KOR
 * @return list of contributors for the project.
 */
@Service("GetContributersForProjectRepo")
public class GetContributersForProjectRepo {

	@Autowired
	GetBitBucketApiConnection apiCon;
	@Autowired
	MaintainerDetailsEmptyRepo maintainerEmpty;
	@Autowired
	GetNoOfForksRepos fork;
	@Autowired
	RepoDetailsRepository repo;
	public List<CommitterDetails> getListOfCommittersForProjectRepos(String projectName,String repoName,int repoId) {
		String repos=apiCon.getBitBucketConnection("https://url/rest/api/1.0/projects/"+projectName+"/repos/"+repoName+"/commits?limit=1000000");
		return getCommits(repos,projectName,repoName,repoId);
	}

	public List<CommitterDetails> getListOfCommitsForProjectReposDeltaScan(String projectName,String repoName,String comitId,int repoId) {
		String commitsJson=apiCon.getBitBucketConnection("https://url/rest/api/1.0/projects/"+projectName+"/repos/"+repoName+"/commits?since="+comitId+" ");
		return getCommits(commitsJson,projectName,repoName,repoId);
	}

	private List<CommitterDetails> getCommits(String repos,String projectName,String repoName,int repoId) {
		List<CommitterDetails> repoUserList=new ArrayList<>();
		RepoDetails commitbean=null;
		try {
			JSONObject jsonbjectRepos = new JSONObject(repos);
			JSONArray jsonArrayRepos = jsonbjectRepos.getJSONArray("values");
			int size=jsonbjectRepos.getInt("size");
			if(size > 0) {
				commitbean=getCommitDate(jsonArrayRepos,size);
			}
			RepoDetails forkbean=fork.getRepoForkCount(projectName, repoName);
			if(commitbean!=null && forkbean!=null) {
				repo.updateCommitAndForkCount(commitbean.getCommitCount(), commitbean.getLatestCommitTime(),
						commitbean.getFirstCommitTime(),
						forkbean.getForkCount(),repoId);
			}
			for (int fileArraySize = 0; fileArraySize < jsonArrayRepos.length(); fileArraySize++) {
				String data=jsonArrayRepos.getJSONObject(fileArraySize).toString();
				if(data.contains("id")) {
					CommitterDetails bean=new CommitterDetails();
					String commitTime=String.valueOf(jsonArrayRepos.getJSONObject(fileArraySize).getLong(("committerTimestamp")));
					long unixSeconds =Long.parseLong(commitTime.substring(0, commitTime.length()-3));
					Date date = new Date(unixSeconds*1000L);
					bean.setTimestamp(date);
					bean.setCommitId(jsonArrayRepos.getJSONObject(fileArraySize).getString("id"));
					String dataCommitter=jsonArrayRepos.getJSONObject(fileArraySize).getJSONObject("committer").toString();
					String dataAuthor=jsonArrayRepos.getJSONObject(fileArraySize).getJSONObject("author").toString();
					if(dataCommitter.contains("id") ) {
						bean.setCommitterId(jsonArrayRepos.getJSONObject(fileArraySize).getJSONObject("committer").getInt("id"));
					}
					else if(dataAuthor.contains("id")) {
						bean.setCommitterId(jsonArrayRepos.getJSONObject(fileArraySize).getJSONObject("author").getInt("id"));
					}else {
					}
					bean.setRepoId(repoId);
					repoUserList.add(bean);
				}else {
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return repoUserList;
	}

	private RepoDetails getCommitDate(JSONArray jsonArrayRepos,int size) {
		RepoDetails repobean=new RepoDetails();
		String timeHashLatestCommit=String.valueOf(jsonArrayRepos.getJSONObject(0).getLong("committerTimestamp"));
		Date latestCommitdate=getTimeinIST(timeHashLatestCommit);
		String timeHashFirstTime=String.valueOf(jsonArrayRepos.getJSONObject(size-1).getLong("committerTimestamp"));
		Date firstCommitdate=getTimeinIST(timeHashFirstTime);
		repobean.setLatestCommitTime(latestCommitdate);
		repobean.setFirstCommitTime(firstCommitdate);
		repobean.setCommitCount(String.valueOf(size));
		return repobean;
	}

	public List<MaintainerDetails> getListOfMaintainersForProjectRepos(String projectKey, String repoName,int repoId) {
		List<MaintainerDetails> repoUserList=new ArrayList<>();
		String repos=apiCon.getBitBucketConnection("https://url/rest/api/1.0/projects/"+projectKey+"/repos/"+repoName+"/permissions/users");
		try {
			JSONObject jsonbjectRepos = new JSONObject(repos);
			int size=jsonbjectRepos.getInt("size");
			if(size > 0) {
				JSONArray jsonArrayRepos = jsonbjectRepos.getJSONArray("values");
				for (int fileArraySize = 0; fileArraySize < jsonArrayRepos.length(); fileArraySize++) {
					MaintainerDetails bean=new MaintainerDetails();
					bean.setMaintainerId(jsonArrayRepos.getJSONObject(fileArraySize).getJSONObject("user").getInt("id"));
					bean.setRepoId(repoId);
					bean.setPermission(jsonArrayRepos.getJSONObject(fileArraySize).getString("permission"));
					repoUserList.add(bean);
				}	
			}else {
				MaintainerDetailsEmpty maintainBean=new MaintainerDetailsEmpty();
				maintainBean.setRepoId(repoId);
				maintainerEmpty.save(maintainBean);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return repoUserList;
	}
	private Date getTimeinIST(String timeHash) {
		long unixSeconds =Long.parseLong(timeHash.substring(0, timeHash.length()-3));
		Date date = new Date(unixSeconds*1000L);
		return date; 
	}

}
