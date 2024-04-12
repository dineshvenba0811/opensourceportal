package com.bosch.opensource.batch.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bosch.opensource.batch.backend.dao.FileDetails;

@Service("GetFileDetails")
public class GetFileDetails {

	@Autowired
	GetBitBucketApiConnection apiCon;
	@Autowired
	ReadFileContents fileContents;
	List <FileDetails> fileBeanList=new ArrayList<>();
	String directoryName="";

	/** iterates over the repo branch and fetch file extension,branch name.
	 * @param projectName
	 * @param repoName
	 * @param dirName
	 * @return fileBeanList
	 */
	public List <FileDetails> getFilesAndDirectory(String projectName,String repoName,String dirName) {
		directoryName=dirName;
		int fileLines=0;
		String fileData=apiCon.getBitBucketConnection("https://url/rest/"
				+ "api/1.0/projects/"+projectName+"/repos/"+repoName+"/browse/"+directoryName);
		try {
			String fileExtension="";
			JSONObject jsonbject = new JSONObject(fileData);
			JSONObject childrenObject =  jsonbject.getJSONObject("children");
			JSONArray jsonArray = childrenObject.getJSONArray("values");
			for(int i=0;i<jsonArray.length();i++) {
				JSONObject path = jsonbject.getJSONObject("path");
				directoryName = path.getString("toString").toString();
				String branch=jsonbject.getString("revision");
				FileDetails fileBean=new FileDetails();
				String type=jsonArray.getJSONObject(i).getString("type");
				if(type.equalsIgnoreCase("FILE")) {
					String fileName=jsonArray.getJSONObject(i).getJSONObject("path").getString("name");
					String extensionDataCheck=jsonArray.getJSONObject(i).getJSONObject("path").toString();
					if(extensionDataCheck.contains("extension")) {
						fileExtension=jsonArray.getJSONObject(i).getJSONObject("path").getString("extension");
						if(!directoryName.isEmpty()) {
							fileLines=fileContents.getFileDetails("https://url/rest/api/1.0/"
									+ "projects/"+projectName+"/repos/"+repoName+"/browse/"+directoryName+"/"+fileName);
						}else {
							fileLines=fileContents.getFileDetails("https://url/rest/api/1.0/"
									+ "projects/"+projectName+"/repos/"+repoName+"/browse/"+fileName);
						}
					}
					fileBean.setProjectKey(projectName);
					fileBean.setRepoName(repoName);
					fileBean.setFileName(fileName);
					fileBean.setFileExtension(fileExtension);
					fileBean.setBranchName(branch);
					fileBean.setLoc(String.valueOf(fileLines));
					fileBeanList.add(fileBean);
				}else {
					JSONObject dirpath = jsonbject.getJSONObject("path");
					directoryName = dirpath.getString("toString").toString();
					String currentDirName=jsonArray.getJSONObject(i).getJSONObject("path").getString("toString");
					if(directoryName.equalsIgnoreCase("")) {
						directoryName=currentDirName;
					}else {
						directoryName=directoryName+"/"+currentDirName;
					}
					getFilesAndDirectory(projectName,repoName,directoryName);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return fileBeanList;
	}

	public List<FileDetails> getFilesOftheRepo(String projectKey, String repoName) {
		List<FileDetails> fileList=new ArrayList<>();
		String fileData=apiCon.getBitBucketConnection("https://url/rest/"
				+ "api/1.0/projects/"+projectKey+"/repos/"+repoName+"/files");
		if(!fileData.isEmpty()) {
			JSONObject jsonbject = new JSONObject(fileData);
			int size=jsonbject.getInt("size");
			if(size > 0) {
				JSONArray valueObject =  jsonbject.getJSONArray("values");
				for(int i=0;i<valueObject.length();i++) {
					FileDetails bean=new FileDetails();
					bean.setFileName(valueObject.getString(i));
					fileList.add(bean);
				}
			}
		}
		return fileList;
	}

	public int  getFileDetails(String fileName,String projectKey, String repoName) {
		String jsonData="";
		int size=0;
		String fileData=apiCon.getBitBucketConnection("https://url/rest/"
				+ "api/1.0/projects/"+projectKey+"/repos/"+repoName+"/browse/"+fileName+" ");
		if(!fileData.isEmpty()) {
			JSONObject jsonbject = new JSONObject(fileData.trim());
			jsonData=jsonbject.toString();
			if(jsonData.contains("size")) {
				size=jsonbject.getInt("size");
			}else {
				return 0;
			}
		}
		else {
			return 0;
		}
		return size;
	}

}
