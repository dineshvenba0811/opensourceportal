package com.bosch.opensource.batch.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bosch.opensource.batch.backend.dao.CategoriesDetails;
import com.bosch.opensource.batch.backend.service.GetBitBucketApiConnection;

@Service("GetCategoryDetails")
public class GetCategoryDetails {
	
	@Autowired
	GetBitBucketApiConnection apiCon;
	
	/** getCategory fetch the category details from the api for the project.
	 * @param projectKey
	 * @return categoryBeanList
	 */
	public List<CategoriesDetails> getCategory(String projectKey) {
		List<CategoriesDetails> categoryBeanList=new ArrayList<>();
		String categoryDetails=apiCon.getBitBucketConnection("https://url/rest/categories/latest/project/"+projectKey);
		try {
			JSONObject jsonbjectRepos = new JSONObject(categoryDetails);
			JSONObject  result=jsonbjectRepos.getJSONObject("result");
			JSONArray jsonArrayRepos = result.getJSONArray("categories");
			for (int ArraySize = 0; ArraySize < jsonArrayRepos.length(); ArraySize++) {
				CategoriesDetails bean=new CategoriesDetails();
				bean.setCategory(jsonArrayRepos.getJSONObject(ArraySize).getString("title"));
				bean.setProjectKey(projectKey);
				categoryBeanList.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return categoryBeanList;
	}
	
}
