package com.example.videoplayer.db;

import java.util.List;

public interface ThreadDAO
{
	/** 
	 * 插入线程信息
	 * @param threadInfo
	 * @return void
	 */ 
	public void insertThread(ThreadInfo threadInfo);
	/** 
	 * 删除线程信息
	 * @param url
	 * @param thread_id
	 * @return void
	 */ 
	public void deleteThread(String url);
	/** 
	 * 更新线程下载进度
	 * @param url
	 * @param thread_id
	 * @return void
	 */ 
	public void updateThread(String url, int thread_id, int finished);
	/** 
	 * 查询文件的线程信�?
	 * @param url
	 * @return
	 * @return List<ThreadInfo>
	 */ 
	public List<ThreadInfo> getThreads(String url);
	
	/** 
	 * 线程信息是否存在
	 * @param url
	 * @param thread_id
	 * @return
	 * @return boolean
	 */ 
	public boolean isExists(String url, int thread_id);
}

