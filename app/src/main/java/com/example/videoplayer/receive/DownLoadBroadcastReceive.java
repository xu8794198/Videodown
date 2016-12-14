package com.example.videoplayer.receive;

import com.example.videoplayer.PhoneApplication;
import com.example.videoplayer.db.FileDAO;
import com.example.videoplayer.db.FileDAOImpl;
import com.example.videoplayer.db.FileInfo;
import com.example.videoplayer.myadapter.FileListAdapter;
import com.example.videoplayer.service.DownloadService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class DownLoadBroadcastReceive extends BroadcastReceiver {
	private FileDAO fileDao = null;
	FileListAdapter myAdapter = FileListAdapter.getInstance(PhoneApplication.getContext(), null);
	public DownLoadBroadcastReceive() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		fileDao = new FileDAOImpl(context);
		if (DownloadService.ACTION_UPDATE.equals(intent.getAction())){
			//继续下载
			int finised = intent.getIntExtra("finished", 0);
			double fileFinish = intent.getDoubleExtra("fileFinish", 0.0);
			String url = intent.getStringExtra("url");
			String id = intent.getStringExtra("id");
			int fileTotal = intent.getIntExtra("fileTotal", 0);
			myAdapter.updateProgress(finised,(int)fileFinish,fileTotal,url,id);
			Log.i("mReceiver","-finised = " + finised);
		}else if (DownloadService.ACTION_STOP.equals(intent.getAction())){
			// 下载停止
			int finised = intent.getIntExtra("finished", 0);
			double fileFinish = intent.getDoubleExtra("fileFinish", 0.0);
			String url = intent.getStringExtra("url");
			String id = intent.getStringExtra("id");
			int fileTotal = intent.getIntExtra("fileTotal", 0);
			myAdapter.updateProgress(finised,(int)fileFinish,fileTotal,url,id);
			Log.i("mReceiver","-finised = " + finised);
		}else if (DownloadService.ACTION_FINISHED.equals(intent.getAction())){
			// 下载结束
			FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
			fileDao.updateFile(fileInfo.getUrl(),  100,"2");
			myAdapter.updateProgress( 100,fileInfo.getLength(),fileInfo.getLength(),fileInfo.getUrl(),fileInfo.getId());
			Toast.makeText(context, fileInfo.getFileName() + "下载完毕", Toast.LENGTH_SHORT).show();
		}
	}

}
