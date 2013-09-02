package com.example.initialdatadownload;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	private ArrayList<HashMap<String, String>> InitialResultList;
	private ArrayList<HashMap<String, String>> BasicResultList;
	private ArrayList<HashMap<String, String>> tempList;
	
	private boolean isDownLoad = false;
	private String temp= "";
	//update check flag 도 있어야 한다.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TextView tx = (TextView)findViewById(R.id.tx1);

		SharePreferece sp = new SharePreferece(getBaseContext());

		isDownLoad = sp.getValue(SharePreferece.isDownLoad, false); //다운로드 된 적이 있는지 검사

		if(isDownLoad == false)
		{
			//다운로드 된 적이 없다면 서버에서 json을 받아온다.
			JSONBasicTask mjsonTask = new JSONBasicTask();
			JSONInitialTask mInitialTask = new JSONInitialTask();
			mInitialTask.execute();
			mjsonTask.execute();
			sp.put(SharePreferece.isDownLoad, true);
		}

		try {
			File file = new File(getFilesDir().getAbsolutePath()+"basic.txt");
			Log.e(null, "file exist?"+file.exists());
			ObjectInputStream ois;
			ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(getFilesDir().getAbsolutePath()+"basic.txt")));
			tempList =(ArrayList<HashMap<String, String>>)ois.readObject();
			
			temp = tempList.get(0).get(JSONBasicInfo.TAG_name);
			
			tx.setText(temp);
			
			ois.close();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class JSONBasicTask extends AsyncTask<Void, Void, JSONObject>
	{
		InputStream mInputStream =null;
		JSONObject mJsonObject=null;
		String JSON="";
		public JSONBasicInfo mJsonBasicInfo;
		@Override
		protected JSONObject doInBackground(Void... url) {
			// TODO Auto-generated method stub

			try
			{
				DefaultHttpClient mhttpClient =new DefaultHttpClient();
				HttpPost mHttpPost =new HttpPost(mJsonBasicInfo.url);

				HttpResponse mhttpHttpResponse= mhttpClient.execute(mHttpPost);
				HttpEntity mHttpEntity =mhttpHttpResponse.getEntity();

				mInputStream=mHttpEntity.getContent();

			}
			catch (UnsupportedEncodingException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			catch (ClientProtocolException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						mInputStream, "UTF-8"));
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					if(!line.contains("<!--"))
						sb.append(line + "\n");
				}
				mInputStream.close();
				JSON = sb.toString();

			} catch (Exception e) {
				Log.e("Buffer Error", "Error converting result " + e.toString());
			}

			// try parse the string to a JSON object
			try {
				mJsonObject = new JSONObject(JSON);
			} catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}
			// return JSON String
			return mJsonObject;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub	
			mJsonBasicInfo = new JSONBasicInfo();	

			mJsonBasicInfo.getData(result);

			BasicResultList = mJsonBasicInfo.getList();
			try {
				ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(getFilesDir().getAbsolutePath()+"/basic.txt")));
				os.writeObject(BasicResultList);
				os.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private class JSONInitialTask extends AsyncTask<Void, Void, JSONObject>
	{
		InputStream mInputStream =null;
		JSONObject mJsonObject=null;
		String JSON="";
		public JSONInitialData mJsoniInitialData;
		@Override
		protected JSONObject doInBackground(Void... url) {
			// TODO Auto-generated method stub

			try
			{
				DefaultHttpClient mhttpClient =new DefaultHttpClient();
				HttpPost mHttpPost =new HttpPost(mJsoniInitialData.url);

				HttpResponse mhttpHttpResponse= mhttpClient.execute(mHttpPost);
				HttpEntity mHttpEntity =mhttpHttpResponse.getEntity();

				mInputStream=mHttpEntity.getContent();

			}
			catch (UnsupportedEncodingException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			catch (ClientProtocolException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						mInputStream, "UTF-8"));
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				mInputStream.close();
				JSON = sb.toString();

			} catch (Exception e) {
				Log.e("Buffer Error", "Error converting result " + e.toString());
			}

			// try parse the string to a JSON object
			try {
				mJsonObject = new JSONObject(JSON);
			} catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}
			// return JSON String
			return mJsonObject;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub	
			mJsoniInitialData = new JSONInitialData();	
			mJsoniInitialData.getData(result);
			InitialResultList = mJsoniInitialData.getList();

			try {
				ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(getFilesDir().getAbsolutePath()+"/initial.txt")));
				os.writeObject(InitialResultList);
				
				os.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}


}
