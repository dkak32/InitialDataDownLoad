package com.example.initialdatadownload;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONBasicInfo {
	public static String url="http://14.63.214.51:8080/schoolinfo/University_List.jsp";
	public static String TAG_list="list";
	public static String TAG_totalCount="totalCount";
	public static String TAG_universitySeq="universitySeq";
	public static String TAG_address = "address";
	public static String TAG_tel = "tel";
	public static String TAG_name = "name";
	public static String TAG_homepage ="homrpage";

	JSONArray contents =null;

	private ArrayList<HashMap<String, String>> resultList;

	public ArrayList<HashMap<String, String>> getData(JSONObject result)
	{
		resultList= new ArrayList<HashMap<String,String>>();
		JSONObject mJsonObject =result;

		try
		{
			HashMap<String, String> map1=new HashMap<String, String>();
			String totalCount = mJsonObject.getString(TAG_totalCount);
			contents=mJsonObject.getJSONArray(TAG_list);
			map1.put(TAG_totalCount, totalCount);
			resultList.add(map1);

			for(int i=0; i<contents.length();i++)
			{
				JSONObject temp =contents.getJSONObject(i);

				String universitySeq = temp.getString(TAG_universitySeq);
				String address = temp.getString(TAG_address);
				String tel= temp.getString(TAG_tel);
				String name= temp.getString(TAG_name);
				String homepage= temp.getString(TAG_homepage);

				
				HashMap<String, String> map=new HashMap<String, String>();
				
				map.put(TAG_universitySeq,universitySeq );
				map.put(TAG_address, address);
				map.put(TAG_tel, tel);
				map.put(TAG_name, name);
				map.put(TAG_homepage, homepage);
				
				resultList.add(map);
				
			}
				
		}
		catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return resultList;
	}
	public ArrayList<HashMap<String, String>> getList()
	{
		return resultList;
	}
}