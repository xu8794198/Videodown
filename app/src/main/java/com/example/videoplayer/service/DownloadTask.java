package com.example.videoplayer.service;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;

import com.example.videoplayer.db.FileDAOImpl;
import com.example.videoplayer.db.FileInfo;
import com.example.videoplayer.db.ThreadDAO;
import com.example.videoplayer.db.ThreadDAOImpl;
import com.example.videoplayer.db.ThreadInfo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DownloadTask
{
	private Context mContext = null;
	private FileInfo mFileInfo = null;
	private ThreadDAO mDao = null;
	private FileDAOImpl fileDao = null;
	public boolean isHaved = false;
	private double mFinised = 0;
	public boolean isPause = false;
	public static List<FileInfo> fList = new ArrayList<FileInfo>();//�?有正在下载的文件的集�?
	private int f;
	private int mThreadCount = 1;  // 线程数量
	private List<DownloadThread> mDownloadThreadList = null; // 线程集合
	
	/** 
	 *@param mContext
	 *@param mFileInfo
	 */
	public DownloadTask(Context mContext, FileInfo mFileInfo, int count)
	{
		this.mContext = mContext;
		this.mFileInfo = mFileInfo;
		this.mThreadCount = count;
		mDao = new ThreadDAOImpl(mContext);
		fileDao = new FileDAOImpl(mContext);
		FileInfo fileInfo = fileDao.getFiles(mFileInfo.getUrl());
		if(fileInfo==null){
			mFileInfo.setState("0");
			fileDao.insertFile(mFileInfo);
		}else if(fileInfo.getFinished() == 100){
			isHaved = true;
		}
	}
	
	public void downLoad()
	{
		fList.add(mFileInfo);
		// 读取数据库的线程信息
		List<ThreadInfo> threads = mDao.getThreads(mFileInfo.getUrl());
		ThreadInfo threadInfo = null;
		
		if (0 == threads.size())
		{
			// 计算每个线程下载长度
			int len = mFileInfo.getLength() / mThreadCount;
			for (int i = 0; i < mThreadCount; i++)
			{
				// 初始化线程信息对�?
				threadInfo = new ThreadInfo(i, mFileInfo.getUrl(),
						len * i, (i + 1) * len - 1, 0);
				
				if (mThreadCount - 1 == i)  // 处理�?后一个线程下载长度不能整除的问题
				{
					threadInfo.setEnd(mFileInfo.getLength());
				}
				
				// 添加到线程集合中
				threads.add(threadInfo);
				mDao.insertThread(threadInfo);
			}
		}

		mDownloadThreadList = new ArrayList<DownloadThread>();
		// 启动多个线程进行下载
		for (ThreadInfo info : threads)
		{
			DownloadThread thread = new DownloadThread(info);
			thread.start();
			// 添加到线程集合中
			mDownloadThreadList.add(thread);
		}
	}
	
	private class DownloadThread extends Thread
	{
		private ThreadInfo mThreadInfo = null;
		public boolean isFinished = false;  // 线程是否执行完毕

		public DownloadThread(ThreadInfo mInfo)
		{
			this.mThreadInfo = mInfo;
		}
		
		@Override
		public void run()
		{
			HttpURLConnection connection = null;
			RandomAccessFile raf = null;
			InputStream inputStream = null;
			
			try
			{
				URL url = new URL(mThreadInfo.getUrl());
				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(5000);
				connection.setRequestMethod("GET");
				// 设置下载位置
				int start = mThreadInfo.getStart() + mThreadInfo.getFinished();
				connection.setRequestProperty("Range", "bytes=" + start + "-" + mThreadInfo.getEnd());
				// 设置文件写入位置
				File file = new File(DownloadService.DOWNLOAD_PATH, mFileInfo.getFileName());
				raf = new RandomAccessFile(file, "rwd");
				raf.seek(start);
				Intent intent = new Intent();
				intent.setAction(DownloadService.ACTION_UPDATE);
				mFinised += mThreadInfo.getFinished();

				// 开始下载
				if (connection.getResponseCode() == HttpStatus.SC_PARTIAL_CONTENT)
				{
					fileDao.updateFile(mThreadInfo.getUrl(), f,"0");
					inputStream = connection.getInputStream();
					byte buf[] = new byte[1024 << 2];
					int len = -1;
					long time = System.currentTimeMillis();

					while ((len = inputStream.read(buf)) != -1) {
						raf.write(buf, 0, len);
						mFinised += len;
						// 累加每个线程完成的进�?
						mThreadInfo.setFinished(mThreadInfo.getFinished() + len);
						f = (int) ((mFinised  / mFileInfo.getLength())*100);
//						Log.i("json","已完成："+ mThreadInfo.getId() + "finished = " + f);
						mFileInfo.setFinished(f);
						if (System.currentTimeMillis() - time > 2000)
						{
							time = System.currentTimeMillis();
							if (f <= mFileInfo.getFinished()){
								intent.putExtra("finished", f);
								intent.putExtra("fileFinish", mFinised);
								intent.putExtra("fileTotal", mFileInfo.getLength());
								intent.putExtra("url", mFileInfo.getUrl());
								intent.putExtra("id", mFileInfo.getId());
								mContext.sendBroadcast(intent);
							}
						}
						
						// 在下载暂停时，保存下载进度
						if (isPause)
						{
							mDao.updateThread(mThreadInfo.getUrl(),	mThreadInfo.getId(),mThreadInfo.getFinished());
							fileDao.updateFile(mThreadInfo.getUrl(), f,"1");
							Log.i("json","mThreadInfo:" + mThreadInfo.getId() + "finished = " + mThreadInfo.getFinished());
							//System.out.println("暂停下载"+ mThreadInfo.getId() + "finished = " + mThreadInfo.getFinished());
							break;
						}
					}
					
					// 标识线程执行完毕
					isFinished = true;
					checkAllThreadFinished();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					if (connection != null)
					{
						connection.disconnect();
					}
					if (raf != null)
					{
						raf.close();
					}
					if (inputStream != null)
					{
						inputStream.close();
					}
				}
				catch (Exception e2)
				{
					e2.printStackTrace();
				}
			}
		}
	}
	
	/** 
	 * 判断所有的线程是否执行完毕
	 * @return void
	 * @author Yann
	 * @date 2015-8-9 下午1:19:41
	 */ 
	private synchronized void checkAllThreadFinished()
	{
		boolean allFinished = true;
		
		// 遍历线程集合，判断线程是否都执行完毕
		for (DownloadThread thread : mDownloadThreadList)
		{
			if (!thread.isFinished)
			{
				allFinished = false;
				break;
			}
		}
		
		if (allFinished)
		{
			if(f < 100){
				fileDao.updateFile(mFileInfo.getUrl(),  mFileInfo.getFinished(),"1");
				Intent intent = new Intent(DownloadService.ACTION_STOP);
				intent.putExtra("fileInfo", mFileInfo);
				mContext.sendBroadcast(intent);
			}else{
				removeFile(mFileInfo);
				// 删除下载记录
				mDao.deleteThread(mFileInfo.getUrl());
				Intent intent = new Intent(DownloadService.ACTION_FINISHED);
				intent.putExtra("fileInfo", mFileInfo);
				mContext.sendBroadcast(intent);
			}
		}
	}
	public void removeFile(FileInfo fileInfo){
		for(FileInfo f : fList){
			if(f.getUrl().equals(fileInfo.getUrl())){
				fList.remove(f);
			}
		}
	}
}

