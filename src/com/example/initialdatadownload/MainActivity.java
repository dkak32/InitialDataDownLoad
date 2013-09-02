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
import android.os.Environment;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ArrayList<HashMap<String, String>> InitialResultList=null;
	private ArrayList<HashMap<String, String>> BasicResultList=null;
	private JSONObject tempObject;
	private JSONBasicInfo mJsonBasicInfo;
	private JSONInitialData mJsonInitialData;
	private String sdPath=Environment.getExternalStorageDirectory().getAbsolutePath();


	private boolean isDownLoad = false;
	private String temp= "";
	//update check flag 도 있어야 한다.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TextView tx = (TextView)findViewById(R.id.tx1);

		SharePreferece sp = new SharePreferece(this);

		isDownLoad = sp.getValue(SharePreferece.isDownLoad, false); //다운로드 된 적이 있는지 검사

		mJsonBasicInfo = new JSONBasicInfo();
		mJsonInitialData = new JSONInitialData();

		if(isDownLoad == false)
		{
			//다운로드 된 적이 없다면 서버에서 json을 받아온다.
			JSONParser mJsonParser =new JSONParser(getApplicationContext());
			mJsonParser.getJSONFromUrl(JSONBasicInfo.url);
			//File로부터 json을 읽어서 가져온다.
			sp.put(SharePreferece.isDownLoad, true);
			
		}
		

		String readStr="";
		try {
			FileInputStream fis = this.openFileInput("json.txt");
			byte []buffer = new byte[fis.available()];	
			fis.read(buffer);

			readStr = new String(buffer);

			JSONObject mjsonObject =new JSONObject(readStr);
			BasicResultList=mJsonBasicInfo.getData(mjsonObject);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("파일읽어오기 실패", e.getMessage());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tx.setText(readStr);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
