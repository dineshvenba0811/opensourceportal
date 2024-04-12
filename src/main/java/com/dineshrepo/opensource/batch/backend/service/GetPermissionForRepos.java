package com.bosch.opensource.batch.backend.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** permission details of the repos.
 * @author DCH2KOR
 * @param project key,reponame
 */
@Service("GetPermissionForRepos")
public class GetPermissionForRepos {

	@Autowired
	GetBitBucketApiConnection apiCon;

	public boolean getPermissionDetailsforRepos(String projectName,String repoName) {
		boolean flag=false;
		String reposAcessDetails=apiCon.getBitBucketConnection("https://url/rest/api/1.0/"
				+ "projects/"+projectName+"/repos/"+repoName+"/permissions/groups");
		try {
			JSONObject jsonbjectRepos = new JSONObject(reposAcessDetails);
			JSONArray jsonArrayRepos = jsonbjectRepos.getJSONArray("values");
			int size=jsonbjectRepos.getInt("size");
			if(size > 0) {
				for (int ArraySize = 0; ArraySize < jsonArrayRepos.length(); ArraySize++) {
					String groupName=jsonArrayRepos.getJSONObject(ArraySize).getJSONObject("group").getString("name");
					if(groupName.equalsIgnoreCase("rb_sde_soco_opensource_user_uf")) {
						flag=true;
					}
				}
			}else {
				flag=true;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return flag;
	}
}
