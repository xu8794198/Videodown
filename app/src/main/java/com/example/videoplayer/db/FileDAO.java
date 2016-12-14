package com.example.videoplayer.db;

import java.util.List;

public interface FileDAO{
	/** 
	 * 插入文件信息
	 * @param threadInfo
	 * @return void
	 */ 
	public void insertFile(FileInfo fileInfo);
	/** 
	 * 删除文件信息
	 * @param url
	 * @param file_id
	 * @return void
	 */ 
	public void deleteFile(String url);
	/** 
	 * 更新文件下载进度
	 * @param url
	 * @param File_id
	 * @return void
	 */ 
	public void updateFile(String url, int finished, String state);
	/** 
	 * 查询文件的文件信息
	 * @param url
	 * @return
	 * @return List<FileInfo>
	 */ 
	public FileInfo getFiles(String url);
	
	public List<FileInfo> query();
	/** 
	 * 文件信息是否存在
	 * @param url
	 * @param File_id
	 * @return
	 * @return boolean
	 */ 
	public boolean isExists(String url);
}

