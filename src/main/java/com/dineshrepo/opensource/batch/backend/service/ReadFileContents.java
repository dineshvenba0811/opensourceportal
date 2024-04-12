package com.bosch.opensource.batch.backend.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bosch.opensource.batch.backend.dao.NoReadMeFiles;
import com.bosch.opensource.batch.backend.dbRepository.NoReadMeFilesRepo;

@Service("ReadFileContents")
public class ReadFileContents {

	@Autowired
	GetBitBucketApiConnection apiCon;

	@Autowired
	NoReadMeFilesRepo readmeRepo;
	public int getFileDetails(String URL) {
		int	totalLines=0;
		String fileContents=apiCon.getBitBucketConnection(URL);
		try {
			JSONObject jsonbject = new JSONObject(fileContents);
			totalLines = jsonbject.getInt("size");
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return totalLines;
	}

	public String getReadMeFileDetails(String projectKey, String repoName) {
		String output="";
		String fileContents=apiCon.getBitBucketConnection("https://url/rest/api/1.0/projects/"+projectKey+"/repos/"+repoName+"/browse");
		JSONObject jsonbject = new JSONObject(fileContents);
		JSONObject childrenJson=jsonbject.getJSONObject("children");
		int totalSize=childrenJson.getInt("size");
		if(totalSize >0) {
			JSONArray valuesArray=childrenJson.getJSONArray("values");
			for(int i=0;i<valuesArray.length();i++) {
				JSONObject pathJson=valuesArray.getJSONObject(i).getJSONObject("path");
				String filename=pathJson.getString("name");
				if(filename.equalsIgnoreCase("readme.md")) {
					output=getFileData(projectKey,repoName,filename);
				}
				else {
					/*
					 * NoReadMeFiles bean=new NoReadMeFiles(); bean.setProjectKey(projectKey);
					 * bean.setRepoName(repoName); readmeRepo.save(bean);
					 */
				}
			}
		}
		return output;
	}

	private String getFileData(String projectKey, String repoName,String filename) {
		String fileContents=apiCon.getBitBucketConnection("https://url/rest/api/1.0/projects/"+projectKey+"/repos/"+repoName+"/browse/"+filename+"");
		int	totalLines=0;
		StringBuilder sb=new StringBuilder();
		try {
			JSONObject jsonbject = new JSONObject(fileContents);
			totalLines = jsonbject.getInt("size");
			if(totalLines > 0) {
				JSONArray jsonArray=jsonbject.getJSONArray("lines");
				for(int i=0;i<jsonArray.length();i++) {
					String text=jsonArray.getJSONObject(i).getString("text");
					sb.append(text);
				}
			}
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
