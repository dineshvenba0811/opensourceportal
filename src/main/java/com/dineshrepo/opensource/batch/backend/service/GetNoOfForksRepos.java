package com.bosch.opensource.batch.backend.service;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.opensource.batch.backend.dao.RepoDetails;

/** fork details for the project repos.
 * @author DCH2KOR
 *
 */
@Service("GetNoOfForksRepos")
public class GetNoOfForksRepos {
	@Autowired
	GetBitBucketApiConnection apiCon;
	public RepoDetails getRepoForkCount(String projectKey,String repoName) {
		RepoDetails bean=new RepoDetails();
		String repos=apiCon.getBitBucketConnection("https://url/rest/api/1.0/projects/"+projectKey+"/repos/"+repoName+"/forks?limit=1000");
		try {
			JSONObject jsonbjectRepos = new JSONObject(repos);
			bean.setForkCount(String.valueOf(jsonbjectRepos.getInt("size")));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bean;
	}
}
