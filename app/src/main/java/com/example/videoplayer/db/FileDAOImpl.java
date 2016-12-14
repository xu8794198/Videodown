package com.example.videoplayer.db;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FileDAOImpl implements FileDAO {
	private DBHelper mHelper = null;
	private String TAG = "json";
	
	public FileDAOImpl(Context context) {
		mHelper = DBHelper.getInstance(context);
	}

	@Override
	public void insertFile(FileInfo fileInfo) {
		Log.i(TAG, "insertFile:"+ fileInfo.getUrl()+","+fileInfo.getFileName()+","
	+ fileInfo.getFinished()+","+fileInfo.getLength());
		SQLiteDatabase db = mHelper.getWritableDatabase();
		db.execSQL("insert into file_info(id,url,name,state,finished,length) values(?,?,?,?,?,?)",
				new Object[] { fileInfo.getId(),fileInfo.getUrl(),
						fileInfo.getFileName(),fileInfo.getState(), fileInfo.getFinished(),
						fileInfo.getLength() });
		db.close();
	}

	@Override
	public void deleteFile(String url) {
		

	}

	@Override
	public void updateFile(String url, int finished,String state) {
		Log.i(TAG, "updateFile:url = "+url+",finished = "+ finished+", state = "+state);
		SQLiteDatabase db = mHelper.getWritableDatabase();
		db.execSQL("update file_info set finished = ?,state = ? where url = ?",new Object[]{finished,state, url});
		db.close();
	}

	@Override
	public FileInfo getFiles(String url) {
		Log.i(TAG, "getFiles:url= "+url);
		FileInfo fileInfo = null;

		SQLiteDatabase db = mHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from file_info where url=?",new String[]{url});
		while (cursor.moveToNext()) {
			fileInfo = new FileInfo();
			fileInfo.setId(cursor.getString(cursor.getColumnIndex("id")));
			fileInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
			fileInfo.setState(cursor.getString(cursor.getColumnIndex("state")));
			fileInfo.setFileName(cursor.getString(cursor.getColumnIndex("name")));
			fileInfo.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
			fileInfo.setLength(cursor.getInt(cursor.getColumnIndex("length")));
		}
		cursor.close();
		db.close();
		return fileInfo;
	}

	@Override
	public List<FileInfo> query() {
		Log.i(TAG, "query ...................");
		List<FileInfo> list = new ArrayList<FileInfo>();

		SQLiteDatabase db = mHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from file_info",null);
		while (cursor.moveToNext()) {
			FileInfo fileInfo = new FileInfo();
			fileInfo.setId(cursor.getString(cursor.getColumnIndex("id")));
			fileInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
			fileInfo.setState(cursor.getString(cursor.getColumnIndex("state")));
			fileInfo.setFileName(cursor.getString(cursor.getColumnIndex("name")));
			fileInfo.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
			fileInfo.setLength(cursor.getInt(cursor.getColumnIndex("length")));
			list.add(fileInfo);
		}
		cursor.close();
		db.close();
		return list;
	}

	@Override
	public boolean isExists(String url) {
		Log.i(TAG, "isExists: url ="+url+",");
		SQLiteDatabase db = mHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from file_info where url = ? ",
				new String[] { url + "" });
		boolean exists = cursor.moveToNext();
		cursor.close();
		db.close();
		return exists;
	}

}
