package com.example.initialdatadownload;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONInitialData {
	public static String url="http://14.63.214.51:8080/unibbs/UniversityBoard_AllList.jsp";

	public static String TAG_list="list";
	public static String TAG_uniSeq="uniSeq";
	public static String TAG_uniName="uniName";
	public static String TAG_board="board";
	public static String TAG_bSeq="bSeq";
	public static String TAG_bName="bName";
	public static String TAG_rows="rows";
	public static String TAG_title="title";
	public static String TAG_rowbSeq="rowbSeq";
	public static String TAG_likeCnt="likeCnt";
	public static String TAG_cnt="cnt";

	JSONArray contents =null;
	JSONArray boards=null;
	JSONArray rows=null;

	private ArrayList<HashMap<String, String>> resultList;

	public ArrayList<HashMap<String, String>> getData(JSONObject result)
	{
		resultList= new ArrayList<HashMap<String,String>>();
		JSONObject mJsonObject =result;

		try
		{
			contents=mJsonObject.getJSONArray(TAG_list);


			for(int i=0; i<contents.length();i++)
			{
				JSONObject ListTemp =contents.getJSONObject(i);
				String uniName = ListTemp.getString(TAG_uniName);
				String uniSeq = ListTemp.getString(TAG_uniSeq);
				
				boards=ListTemp.getJSONArray(TAG_board);

				for(int j = 0; j<boards.length(); j++)
				{
					JSONObject BoardTemp = boards.getJSONObject(j);
					
					String bSeq = BoardTemp.getString(TAG_bSeq);
					String bName = BoardTemp.getString(TAG_bName);
					
					rows=BoardTemp.getJSONArray(TAG_rows);
					for(int k = 0; k <rows.length(); k++)
					{
						JSONObject RowTemp = rows.getJSONObject(k);

						HashMap<String, String> RowMap=new HashMap<String, String>();

						String title = RowTemp.getString(TAG_title);
						String rowbSeq = RowTemp.getString(TAG_bSeq);
						String likeCnt = RowTemp.getString(TAG_likeCnt);
						String cnt = RowTemp.getString(TAG_cnt);

						RowMap.put(TAG_uniSeq, uniSeq);
						RowMap.put(TAG_uniName, uniName);
						RowMap.put(TAG_bSeq, bSeq);
						RowMap.put(TAG_bName, bName);
						RowMap.put(TAG_title, title);
						RowMap.put(TAG_rowbSeq, rowbSeq);
						RowMap.put(TAG_likeCnt, likeCnt);
						RowMap.put(TAG_cnt, cnt);
						

						resultList.add(RowMap);
					}
				

				}
			
				
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