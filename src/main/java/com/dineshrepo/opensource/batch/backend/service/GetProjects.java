package com.bosch.opensource.batch.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bosch.opensource.batch.backend.dao.opensourcedao;
import com.bosch.opensource.batch.backend.dao.ProjectDetailsTwo;


/** get all projects from the server
 * @author DCH2KOR
 *
 */
@Service("GetProjects")
public class GetProjects {
	@Autowired
	GetBitBucketApiConnection apiCon;
	@Autowired
	opensourcedao dao;
	private static final String emptyString = "";

	public List<ProjectDetailsTwo>  getProjects() throws JSONException {
		List<ProjectDetailsTwo> projectList=new ArrayList<>();
		int startAt=0;
		boolean flag=true;
		do {
			String apiDataJson=apiCon.getBitBucketConnection("https://url/rest/api/1.0/projects?start="+startAt+"&limit=1000");
			JSONObject jsonbject = new JSONObject(apiDataJson);
			JSONArray jsonArray = jsonbject.getJSONArray("values");
			for (int i = 0; i < jsonArray.length(); i++) {
				ProjectDetailsTwo projectBean=new ProjectDetailsTwo();
				projectBean.setProjectKey(jsonArray.getJSONObject(i).getString("key"));
				projectBean.setProjectName(jsonArray.getJSONObject(i).getString("name"));
				projectBean.setProjectId(jsonArray.getJSONObject(i).getInt("id"));
				if(jsonArray.getJSONObject(i).toString().contains("description")) {
					projectBean.setProjectDescription(jsonArray.getJSONObject(i).getString("description"));
				}else {
					projectBean.setProjectDescription(emptyString);
				}
				projectBean.setBitBucketUrl(jsonArray.getJSONObject(i).getJSONObject("links").getJSONArray("self").toString());
				projectList.add(projectBean);
				flag=jsonbject.getBoolean("isLastPage");
				int size=jsonbject.getInt("size");
				if(size >999) {
					startAt=jsonbject.getInt("nextPageStart");
				}
			}
		}while(!flag);
		return projectList;
	}

	public List<ProjectDetailsTwo> checkWithCategory() {
		dao.getConnection();
		return dao.getopensourceProjectWithCategory();
		
	}

	public List<ProjectDetailsTwo> getRemainingProjects() {
		dao.getConnection();
		return dao.getProjects();
	}
}