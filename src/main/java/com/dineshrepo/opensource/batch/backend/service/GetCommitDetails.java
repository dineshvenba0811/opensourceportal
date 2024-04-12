package com.bosch.opensource.batch.backend.service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.opensource.batch.backend.dao.RepoDetails;

/** get the commit details of the project repo.
 * @author DCH2KOR
 * @param projectkey,reponame.
 * @return bean data
 */
@Service("GetCommitDetails")
public class GetCommitDetails {
	@Autowired
	GetBitBucketApiConnection apiCon;
	public RepoDetails getCommitDetails(String projectKey,String repoName) {
		RepoDetails bean=new RepoDetails();
		String repos=apiCon.getBitBucketConnection("https://url/rest/api/1.0/projects/"+projectKey+"/repos/"+repoName+"/commits?limit=1000000");
		try {
			JSONObject jsonbjectRepos = new JSONObject(repos);
			int size=jsonbjectRepos.getInt("size");
			if(size > 0) {
				JSONArray jsonArrayObj=jsonbjectRepos.getJSONArray("values");
				String timeHashLatestCommit=String.valueOf(jsonArrayObj.getJSONObject(0).getLong("authorTimestamp"));
				Date latestCommitdate=getTimeinIST(timeHashLatestCommit);
				String timeHashFirstTime=String.valueOf(jsonArrayObj.getJSONObject(size-1).getLong("authorTimestamp"));
				Date firstCommitdate=getTimeinIST(timeHashFirstTime);
				bean.setLatestCommitTime(latestCommitdate);
				bean.setFirstCommitTime(firstCommitdate);
				bean.setCommitCount(String.valueOf(jsonbjectRepos.getInt("size")));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bean;

	}
	private Date getTimeinIST(String timeHash) {
		long unixSeconds =Long.parseLong(timeHash.substring(0, timeHash.length()-3));
		Date date = new Date(unixSeconds*1000L);
		return date; 
	}
}
