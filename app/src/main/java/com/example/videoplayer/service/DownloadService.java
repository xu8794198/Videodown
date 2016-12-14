package com.example.videoplayer.service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.HttpStatus;

import com.example.videoplayer.PhoneApplication;
import com.example.videoplayer.db.FileInfo;
import com.example.videoplayer.entity.NetUtils;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class DownloadService extends Service {
	public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
			+ "/phonestudy_downloads/";
	public static final String ACTION_START = "ACTION_START";
	public static final String ACTION_STOP = "ACTION_STOP";
	public static final String ACTION_UPDATE = "com.example.videoplayer.receive.ACTION_UPDATE";
	public static final String ACTION_FINISHED = "com.example.videoplayer.receive.ACTION_FINISHED";
	public static final String ACTION_FINISHED_ALL = "com.example.videoplayer.receive.ACTION_FINISHED_ALL";
	public static final int MSG_INIT = 0;
	private String TAG = "json";
	private Map<String, DownloadTask> mTasks = new LinkedHashMap<String, DownloadTask>();
	public DownloadService() {
	}

	@Override
	public void onCreate() {
		super.onCreate();

		Log.i(TAG, "下载服务创建......");
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		// 获得Activity传过来的参数
		if (ACTION_START.equals(intent.getAction())){
			//网络判断
			/*if("WIFI".equals(NetUtils.getNetworkState().toString())){
			}else if("MOBILE".equals(NetUtils.getNetworkState().toString())){
			}else{
			}**/
			FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
			for(FileInfo f : DownloadTask.fList){
				if(f.getUrl().equals(fileInfo.getUrl())){
					Toast.makeText(PhoneApplication.getContext(), "正在下载", Toast.LENGTH_SHORT).show();
					return super.onStartCommand(intent, flags, startId);
				}
			}
			Toast.makeText(PhoneApplication.getContext(), "开始下载", Toast.LENGTH_SHORT).show();
			fileInfo.setDownLoading(false);
			Log.i(TAG , "Start:" + fileInfo.toString());
			new InitThread(fileInfo).start();
		}
		else if (ACTION_STOP.equals(intent.getAction())) {
			FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
			Log.i(TAG , "Stop:" + fileInfo.toString());
			fileInfo.setDownLoading(true);
			DownloadTask task = mTasks.get(fileInfo.getUrl());
			if (task != null){
				task.removeFile(fileInfo);//防止service网络请求失败，下载列表中存在该数据
				task.isPause = true;
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}
	private Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			switch (msg.what)
			{
				case MSG_INIT:
					FileInfo fileInfo = (FileInfo) msg.obj;
					Log.i(TAG, "Init:" + fileInfo);
					DownloadTask task = new DownloadTask(DownloadService.this, fileInfo, 2);
					if(task.isHaved){
						Toast.makeText(PhoneApplication.getContext(), "文件已下载完成", Toast.LENGTH_SHORT).show();
						return;
					}
					task.downLoad();
					mTasks.put(fileInfo.getUrl(), task);
					break;

				default:
					break;
			}
		};
	};
	
	private class InitThread extends Thread{
		private FileInfo mFileInfo = null;
		public InitThread(FileInfo mFileInfo){
			this.mFileInfo = mFileInfo;
		}
		
		@Override
		public void run()
		{
			HttpURLConnection connection = null;
			RandomAccessFile raf = null;
			try{
				URL url = new URL(mFileInfo.getUrl());
				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(5000);
				connection.setRequestMethod("GET");
				int length = -1;
				
				if (connection.getResponseCode() == HttpStatus.SC_OK) {
					length = connection.getContentLength();
				}
				
				if (length <= 0) {
					return;
				}
				
				File dir = new File(DOWNLOAD_PATH);
				if (!dir.exists()) {
					dir.mkdir();
				}
				String fileName = new String(mFileInfo.getFileName().getBytes("iso-8859-1"),"utf-8");
				File file = new File(dir, fileName);
				if(!file.exists()){
					raf = new RandomAccessFile(file, "rwd");
					raf.setLength(length);
				}
				mFileInfo.setLength(length);
				mHandler.obtainMessage(MSG_INIT, mFileInfo).sendToTarget();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				if (connection != null) {
					connection.disconnect();
				}
				if (raf != null) {
					try {
						raf.close();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onDestroy() {
		Log.i(TAG, "下载服务销毁......");
		super.onDestroy();
	}
}
