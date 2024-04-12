package com.bosch.opensource.batch.backend.service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

/** GetBitBucketApiConnection check connection with apiurl .
 * @args apiurl.
 * @author DCH2KOR
 * @returns data returned from the api in the string format.
 */
@Service("GetBitBucketApiConnection")
public class GetBitBucketApiConnection {

	public String getBitBucketConnection (String url) {
		URLConnection connection = null;
		StringBuilder sb=new StringBuilder();
		URL jiraURL;
		String dataOutput="";
		try {
			jiraURL = new URL(url);
			String login ="***";
			byte[] encodedBytes = Base64.getEncoder().encode(login.getBytes());
			String credentials = new String(encodedBytes);
			connection =  jiraURL.openConnection();
			((HttpURLConnection) connection).setRequestMethod("GET");
			connection.setRequestProperty("Accept", "*/*");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Authorization", "Basic " + credentials);
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			int d=((HttpURLConnection) connection).getResponseCode();
			connection.connect();
			BufferedReader in=new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputline="";
			while((inputline=in.readLine())!=null) {
				sb.append(inputline);
			}
			in.close();
			dataOutput=sb.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}catch (IOException e) {

		}
		return dataOutput;
	}

	
}
