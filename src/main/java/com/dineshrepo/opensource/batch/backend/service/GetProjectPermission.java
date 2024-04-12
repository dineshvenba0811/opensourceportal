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

import com.bosch.opensource.batch.backend.dao.ProjectAdminDetails;
import com.bosch.opensource.batch.backend.dao.ProjectAdminDetailsEmpty;
import com.bosch.opensource.batch.backend.dao.ProjectDetailsEmpty;
import com.bosch.opensource.batch.backend.dao.ProjectDetailsTwo;
import com.bosch.opensource.batch.backend.dao.RepoDetails;
import com.bosch.opensource.batch.backend.dbRepository.ProjectAdminDetailsEmptyRepo;
import com.bosch.opensource.batch.backend.dbRepository.ProjectDetailsEmptyRepo;
/** permission details of the project
 * @author DCH2KOR
 * @param projectkey
 */
@Service("GetProjectPermission")
public class GetProjectPermission {

	@Autowired
	GetBitBucketApiConnection apiCon;
	@Autowired
	ProjectDetailsEmptyRepo project;
	@Autowired
	ProjectAdminDetailsEmptyRepo adminEmptyRepo;
	
	/**getPermissionDetailsforProjects check for the project permission in the api, if project permission were not available,saves in the 
	 * another table to check further.
	 * @param projectKey
	 * @param projectId
	 * @param projectName
	 * @return flag
	 */
	public boolean getPermissionDetailsforProjects(String projectKey,int projectId,String projectName,String bitBucketUrl,String description) {
		boolean flag=false;
		String reposAcessDetails=apiCon.getBitBucketConnection("https://url/rest/api/1.0/"
				+ "projects/"+projectKey+"/permissions/groups");
		try {
			JSONObject jsonbjectRepos = new JSONObject(reposAcessDetails);
			int size=jsonbjectRepos.getInt("size");
			if(size > 0) {
				JSONArray jsonArrayRepos = jsonbjectRepos.getJSONArray("values");
				for (int ArraySize = 0; ArraySize < jsonArrayRepos.length(); ArraySize++) {
					String groupName=jsonArrayRepos.getJSONObject(ArraySize).getJSONObject("group").getString("name");
					if(groupName.equalsIgnoreCase("rb_sde_soco_opensource_user_uf")) {
						flag=true;
					}
				}
			}else {
				ProjectDetailsEmpty bean=new ProjectDetailsEmpty();
				bean.setProjectId(projectId);
				bean.setProjectKey(projectKey);
				bean.setProjectName(projectName);
				bean.setProjectDescription(description);
				bean.setBitBucketUrl(bitBucketUrl);
				project.save(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	/** getAdminforProjects method gets the admin for the projects.
	 * @param projectKey
	 * @param projectId
	 * @return
	 */
	public List<ProjectAdminDetails> getAdminforProjects(String projectKey,int projectId) {
		List<ProjectAdminDetails> projectAdminList=new ArrayList<>();
		String admindata=apiCon.getBitBucketConnection("https://url/rest/api/1.0/projects/"+projectKey+"/permissions/users/");
		try {
			JSONObject jsonbject = new JSONObject(admindata);
			JSONArray jsonArray = null;
			jsonArray = jsonbject.getJSONArray("values");
			for (int arraySize = 0; arraySize < jsonArray.length(); arraySize++) {
				String permission=jsonArray.getJSONObject(arraySize).getString("permission");
				if(permission.equalsIgnoreCase("PROJECT_ADMIN")) {
					ProjectAdminDetails bean=new ProjectAdminDetails();
					bean.setAdminUserId(jsonArray.getJSONObject(arraySize).getJSONObject("user").getInt("id"));
					bean.setProjectId(projectId);
					projectAdminList.add(bean);
				}else {
					ProjectAdminDetailsEmpty emptyBean=new ProjectAdminDetailsEmpty();
					emptyBean.setProjectId(projectId);
					adminEmptyRepo.save(emptyBean);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return projectAdminList;
	}

	public List<ProjectAdminDetails> getNewAdminsForExistingProjects(String projectKey,int projectId) {
		List<ProjectAdminDetails> newProjectAdminList=new ArrayList<>();
		String newRepojson=apiCon.getBitBucketConnection("https://url/rest/audit/1.0/projects/"+projectKey+"/events");
		JSONObject jsonbjectRepos = new JSONObject(newRepojson);
		JSONArray jsonArrayRepos = jsonbjectRepos.getJSONArray("values");
		Date yestrday= yesterday();
		for(int i=0;i<jsonArrayRepos.length();i++) {
			String repoAction=jsonArrayRepos.getJSONObject(i).getString("action");
			if(repoAction.equalsIgnoreCase("ProjectPermissionGrantedEvent")) {
				String timeHash=String.valueOf(jsonArrayRepos.getJSONObject(i).getLong("timestamp"));
				Date changedDate=getTimeinIST(timeHash);
				if(changedDate.toString().equalsIgnoreCase(yestrday.toString())) {
					String details=jsonArrayRepos.getJSONObject(i).getString("details");
					String permissionDetails[]=details.split(",");
					String permissionType=permissionDetails[0];
					if(permissionType.contains("PROJECT_ADMIN")) {
						ProjectAdminDetails bean=new ProjectAdminDetails();
						String usernameData=permissionDetails[1];
						String user=usernameData.substring(usernameData.lastIndexOf(":")+2, usernameData.length()-2);
						String userJson=apiCon.getBitBucketConnection("https://url/rest/api/1.0/projects/"+projectKey+"/permissions/users?filter="+user);
						JSONObject userJsonObject = new JSONObject(userJson);
						JSONArray jsonArrayUsers = userJsonObject.getJSONArray("values");
						bean.setAdminUserId(jsonArrayUsers.getJSONObject(0).getJSONObject("user").getInt("id"));
						bean.setProjectId(projectId);
						newProjectAdminList.add(bean);
					}
				}
			}
		}
		return newProjectAdminList;
	}
	private Date getTimeinIST(String changedDate) {
		long unixSeconds =Long.parseLong(changedDate.substring(0, changedDate.length()-3));
		return new Date(unixSeconds*1000L); 
	}
	private static Date yesterday() {
		final Calendar cal = Calendar.getInstance();
	//	cal.add(Calendar.DATE, -3);
		java.util.Date  date=cal.getTime();
		java.sql.Date sDate = new java.sql.Date(date.getTime());
		return sDate ;
	}
}
