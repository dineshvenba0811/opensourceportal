package com.bosch.opensource.batch.backend.service;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.opensource.batch.backend.dao.UserDetails;

@Service("GetUsers")
public class GetUsers {
	@Autowired
	GetBitBucketApiConnection apiCon;
	public List<UserDetails> getAllUsers() {
		List<UserDetails> userDetailsList=new ArrayList<>();
		boolean flag=true;
		int startAt=0;
		do {
			String userJson=apiCon.getBitBucketConnection("https://url/rest/api/1.0/users?start="+startAt+"&limit=1000");
			JSONObject jsonbjectRepos = new JSONObject(userJson);
			JSONArray jsonArrayRepos = jsonbjectRepos.getJSONArray("values");
			for (int fileArraySize = 0; fileArraySize < jsonArrayRepos.length(); fileArraySize++) {
				UserDetails bean=new UserDetails();
				String data=jsonArrayRepos.getJSONObject(fileArraySize).toString();
				if(data.contains("displayName")) {
					bean.setDisplayName(jsonArrayRepos.getJSONObject(fileArraySize).getString("displayName"));
					String userName=jsonArrayRepos.getJSONObject(fileArraySize).getString("displayName");
					if(userName.contains("(") && userName.contains(")") ) {
						int initial=userName.indexOf("(");
						int last=userName.indexOf(")");
						bean.setBusinessUnit(userName.substring(initial+1, last));
					}else {
						bean.setBusinessUnit("NA");
					}
					
				}else {
					bean.setDisplayName("NA");
					bean.setBusinessUnit("NA");
				}
				if(data.contains("emailAddress")) {
					bean.setEmailId(jsonArrayRepos.getJSONObject(fileArraySize).getString("emailAddress"));
				}else {
					bean.setEmailId("NA");
				}
				if(data.contains("name")) {
					bean.setName(jsonArrayRepos.getJSONObject(fileArraySize).getString("name"));
				}else {
					bean.setName("NA");
				}
				if(data.contains("slug")) {
					bean.setSlug(jsonArrayRepos.getJSONObject(fileArraySize).getString("slug"));
				}else {
					bean.setSlug("NA");
				}
				bean.setId(jsonArrayRepos.getJSONObject(fileArraySize).getInt("id"));
				userDetailsList.add(bean);
			}
			flag=jsonbjectRepos.getBoolean("isLastPage");
			int size=jsonbjectRepos.getInt("size");
			if(size >999) {
				startAt=jsonbjectRepos.getInt("nextPageStart");
			}
		}while(!flag);
		return userDetailsList;
	}
}
