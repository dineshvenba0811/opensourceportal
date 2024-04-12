package com.bosch.opensource.batch.backend.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bosch.opensource.batch.backend.dao.RepoDetails;

@Service("GetReposForProjects")
public class GetReposForProjects {

	@Autowired
	GetBitBucketApiConnection apiCon;
	@Autowired GetPermissionForRepos repoPermission;
	@Autowired
	ReadFileContents fileContents;
	/** getRepoNames method get the repo for the project.
	 * @param projectKey
	 * @return repoList
	 */
	public List<RepoDetails> getRepoNames(String projectKey) {
		List<RepoDetails> newRepoDetailsList=new ArrayList<>();
		String repos=apiCon.getBitBucketConnection("https://url/rest/api/1.0/projects/"+projectKey+"/repos?limit=1000");
		try {
			JSONObject jsonbjectRepos = new JSONObject(repos);
			JSONArray jsonArrayRepos = jsonbjectRepos.getJSONArray("values");
			for (int fileArraySize = 0; fileArraySize < jsonArrayRepos.length(); fileArraySize++) {
				RepoDetails bean=new RepoDetails();
				bean.setRepoId(jsonArrayRepos.getJSONObject(fileArraySize).getInt("id"));
				bean.setRepoName(jsonArrayRepos.getJSONObject(fileArraySize).getString("slug"));
				bean.setState(jsonArrayRepos.getJSONObject(fileArraySize).getString("state"));
				bean.setProjectId(jsonArrayRepos.getJSONObject(fileArraySize).getJSONObject("project").getInt("id"));
				bean.setProjectKey(jsonArrayRepos.getJSONObject(fileArraySize).getJSONObject("project").getString("key"));
				boolean resultFlag=repoPermission.getPermissionDetailsforRepos(projectKey, jsonArrayRepos.getJSONObject(fileArraySize).getString("slug"));
				String reponame=jsonArrayRepos.getJSONObject(fileArraySize).getString("slug");
				String repoDes=fileContents.getReadMeFileDetails(projectKey,reponame);
				if(repoDes.length() < 4995) {
					bean.setDescription(repoDes);
				}
				else {
					bean.setDescription(repoDes.substring(0, 4996));
				}
				if(resultFlag) {
					newRepoDetailsList.add(bean);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return newRepoDetailsList;
	}
	public List<RepoDetails> getNewRepoForExistingProjects(String projectKey) {
		List<RepoDetails> newRepoDetailsList=new ArrayList<>();
		String newRepojson=apiCon.getBitBucketConnection("https://url/rest/audit/1.0/projects/"+projectKey+"/events");
		JSONObject jsonbjectRepos = new JSONObject(newRepojson);
		JSONArray jsonArrayRepos = jsonbjectRepos.getJSONArray("values");
		Date yestrday= yesterday();
		for(int i=0;i<jsonArrayRepos.length();i++) {
			String repoAction=jsonArrayRepos.getJSONObject(i).getString("action");
			if(repoAction.equalsIgnoreCase("RepositoryCreatedEvent")) {
				String timeHash=String.valueOf(jsonArrayRepos.getJSONObject(i).getLong("timestamp"));
				Date changedDate=getTimeinIST(timeHash);
				if(changedDate.toString().equalsIgnoreCase(yestrday.toString())) {
					String details=jsonArrayRepos.getJSONObject(i).getString("details");
					int index=details.lastIndexOf(':');
					String repoName=details.substring(index+2, details.length()-2);
					String repoDetails=apiCon.getBitBucketConnection("https://url/rest/api/1.0/projects/"+projectKey+"/repos/"+repoName);
					RepoDetails bean=getNewRepoDetails(repoDetails,projectKey);
					if(bean!=null) {
						newRepoDetailsList.add(bean);
					}
				}
			}
		}
		return newRepoDetailsList;
	}
	private RepoDetails getNewRepoDetails(String repoDetails,String projectKey) {
		JSONObject jsonbjectRepos = new JSONObject(repoDetails);
		RepoDetails bean=new RepoDetails();
		bean.setRepoId(jsonbjectRepos.getInt("id"));
		bean.setRepoName(jsonbjectRepos.getString("slug"));
		bean.setState(jsonbjectRepos.getString("state"));
		bean.setProjectId(jsonbjectRepos.getJSONObject("project").getInt("id"));
		bean.setProjectKey(jsonbjectRepos.getJSONObject("project").getString("key"));
		boolean resultFlag=repoPermission.getPermissionDetailsforRepos(projectKey,jsonbjectRepos.getString("slug"));
		String repoDes=fileContents.getReadMeFileDetails(projectKey,jsonbjectRepos.getString("slug"));
		if(repoDes.length() < 4995) {
			bean.setDescription(repoDes);
		}
		else {
			bean.setDescription(repoDes.substring(0, 4996));
		}
		if(resultFlag) {
			return bean;
		}
		return null;
	}
	private Date getTimeinIST(String changedDate) {
		long unixSeconds =Long.parseLong(changedDate.substring(0, changedDate.length()-3));
		return new Date(unixSeconds*1000L); 
	}
	private static Date yesterday() {
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -3);
		java.util.Date  date=cal.getTime();
		java.sql.Date sDate = new java.sql.Date(date.getTime());
		return sDate ;
	}
}

