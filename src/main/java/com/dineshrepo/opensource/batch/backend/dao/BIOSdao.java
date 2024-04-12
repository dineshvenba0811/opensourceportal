package com.dineshrepo.opensource.batch.backend.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;


/** opensourcedao establishes connection with the database opensource.
 * @author DCH2KOR
 * return connection object.
 */
@Service("opensourcedao")
public class opensourcedao {
	public   Connection con = null;
	public  Connection getConnection(){  
		try{  
			String url = "****";//10.47.52.220   //localhost
			Class.forName("com.mysql.jdbc.Driver");
			try
			{
				con = DriverManager.getConnection(url, "***", "**"); //mysql.sys
				System.out.println("MYSQL Connection successfully Created.");
			}
			catch(SQLException ex)
			{
				ex.printStackTrace();
			} 
		}catch(Exception e){System.out.println(e);}  
		return con;  
	}
	public void insertintobatchdetailstable(String Startdate, String enddate,String result) {
		try {
			PreparedStatement issueticketsinert=con.prepareStatement("insert into opensource.batchdetails values(?,?,?)");
			issueticketsinert.setString(1, Startdate);
			issueticketsinert.setString(2, enddate);
			issueticketsinert.setString(3, result);
			issueticketsinert.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}
	public void updatebatchdetails(String startdate, String enddate, String result) {
		try {
			PreparedStatement issueticketsinert=con.prepareStatement("update "
					+ "opensource.batchdetails set enddate=?,result=? where startdate=? ");
			issueticketsinert.setString(1, enddate);
			issueticketsinert.setString(2, result);
			issueticketsinert.setString(3, startdate);
			issueticketsinert.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public List<ProjectDetailsTwo> getopensourceProjectWithCategory(){
		List <ProjectDetailsTwo> projectList=new ArrayList<>();
		PreparedStatement getProjectQuery=null;
		ResultSet rs=null;
		try {
			getProjectQuery=con.prepareStatement(" select * from opensource.categories_details inner join opensource.project_details_empty on\n" + 
					"project_details_empty.project_id=categories_details.category_id \n" + 
					"where opensource.categories_details.category='dineshrepo-internal-open-source' ;  ");
			rs=getProjectQuery.executeQuery();
			while(rs.next()) {
				ProjectDetailsTwo beanData=new ProjectDetailsTwo();
				beanData.setProjectName(rs.getString("project_name"));
				beanData.setProjectDescription(rs.getString("project_description"));
				beanData.setBitBucketUrl(rs.getString("bit_bucket_url"));
				beanData.setProjectKey(rs.getString("project_key"));
				beanData.setProjectId(rs.getInt("project_id"));
				projectList.add(beanData);
			}
		} catch (SQLException e) {
		}
		finally {
			if(getProjectQuery !=null) {
				try {
					getProjectQuery.close();
				} catch (SQLException e) {
				}
			}
			if(rs !=null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
		}
		return projectList;
	}
	public List<ProjectDetailsTwo> getProjects() {
		List <ProjectDetailsTwo> projectList=new ArrayList<>();
		PreparedStatement getProjectQuery=null;
		ResultSet rs=null;
		try {
			getProjectQuery=con.prepareStatement(" select * from opensource.categories_details inner join opensource.project_details_empty on\n" + 
					"project_details_empty.project_id=categories_details.category_id \n" + 
					"where opensource.categories_details.category='dineshrepo-internal-open-source' ;  ");
			rs=getProjectQuery.executeQuery();
			while(rs.next()) {
				ProjectDetailsTwo beanData=new ProjectDetailsTwo();
				beanData.setProjectName(rs.getString("project_name"));
				beanData.setProjectDescription(rs.getString("project_description"));
				beanData.setBitBucketUrl(rs.getString("bit_bucket_url"));
				beanData.setProjectKey(rs.getString("project_key"));
				beanData.setProjectId(rs.getInt("project_id"));
				projectList.add(beanData);
			}
		} catch (SQLException e) {
		}
		finally {
			if(getProjectQuery !=null) {
				try {
					getProjectQuery.close();
				} catch (SQLException e) {
				}
			}
			if(rs !=null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
		}
		return projectList;
	}

}
