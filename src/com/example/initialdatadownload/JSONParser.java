package com.example.initialdatadownload;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class JSONParser {
	 
    InputStream is = null;
    JSONObject jObj = null;
    static String json = "";
 
    // constructor
    public JSONParser() {
 
    }
 
    public JSONObject getJSONFromUrl(String url) {
 
        JSONConnect jConnect = new JSONConnect();
        jConnect.execute(url);
        try {
			jObj = jConnect.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // return JSON String
        return jObj;
 
    }
    
    class JSONConnect extends AsyncTask<String, Integer, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			
			String url = params[0];
			// Making HTTP request
	        try {
	            // defaultHttpClient
	            DefaultHttpClient httpClient = new DefaultHttpClient();
	            HttpPost httpPost = new HttpPost(url);
	 
	            HttpResponse httpResponse = httpClient.execute(httpPost);
	            HttpEntity httpEntity = httpResponse.getEntity();
	            is = httpEntity.getContent();           
	 
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	         
	        try {
	            BufferedReader reader = new BufferedReader(new InputStreamReader(
	                    is, "UTF-8"), 8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
					if(!line.contains("<!--"))	// 주석 무시
	                	sb.append(line + "\n");
	            }
	            is.close();
	            json = sb.toString();
	        } catch (Exception e) {
	            Log.e("Buffer Error", "Error converting result " + e.toString());
	        }
	 
	        JSONObject jsonObj = null;
	        // try parse the string to a JSON object
	        try {
	        	jsonObj = new JSONObject(json);
	        } catch (JSONException e) {
	            Log.e("JSON Parser", "Error parsing data " + e.toString());
	        }
			return jsonObj;
		}
    	
    }
}