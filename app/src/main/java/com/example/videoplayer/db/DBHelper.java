package com.example.videoplayer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 数据库帮助类
 */
public class DBHelper extends SQLiteOpenHelper{
	
	private static final String DB_NAME = "download.db";
	private static final int VERSION = 1;
	private static final String SQL_CREATE = "create table thread_info(_id integer primary key autoincrement," +
			"thread_id integer, url varchar(512), start integer, end integer, finished integer)";
	private static final String SQL_DROP = "drop table if exists thread_info";
	
	private static final String SQL_CREATE_FILE = "create table IF NOT EXISTS file_info(_id integer primary key autoincrement," +
			"id char(8), url varchar(512), name varchar(512),state char(2), finished integer,length integer)";
	private static final String SQL_DROP_FILE = "drop table if exists file_info";
	
	private static DBHelper sDbHelper = null;
	
	/** 
	 *@param context
	 *@param name
	 *@param factory
	 *@param version
	 */
	private DBHelper(Context context)
	{
		super(context, DB_NAME, null, VERSION);
	}

	public static DBHelper getInstance(Context context)
	{
		if (null == sDbHelper)
		{
			sDbHelper = new DBHelper(context);
		}
		
		return sDbHelper;
	}
	
	/**
	 * @see SQLiteOpenHelper#onCreate(SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(SQL_CREATE);
		db.execSQL(SQL_CREATE_FILE);
	}

	/**
	 * @see SQLiteOpenHelper#onUpgrade(SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL(SQL_DROP);
		db.execSQL(SQL_CREATE);
		
		db.execSQL(SQL_DROP_FILE);
		db.execSQL(SQL_CREATE_FILE);
	}

}

