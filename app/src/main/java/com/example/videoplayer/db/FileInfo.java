package com.example.videoplayer.db;

import java.io.Serializable;

public class FileInfo implements Serializable
{
	private static final long serialVersionUID = -2758776481713806821L;

	private String id;
	private String url;//文件url
	private String fileName;//文件name
	private int length;//文件长度
	private int finished;//完成的百分比
	private String fileTotal;//文件总大小
	private String fileFinish;//完成了多少kb
	private String state;//0 正在下载;1 暂停;2 完成
	private boolean isHaved = false;//是否下载完成
	private boolean isDownLoading ;//是否正下载
	public FileInfo() {}
	/** 
	 *@param id
	 *@param url
	 *@param fileName
	 *@param length
	 *@param finished
	 */
	public FileInfo(String id,String url, String fileName)
	{
		this(id, url, fileName,"0KB","0KB", 0, 0);
	}
	public FileInfo(String id,String url, String fileName,String fileTotal,String fileFinish, int length,int finished)
	{
		this.id = id;
		this.url = url;
		this.fileName = fileName;
		this.fileTotal = fileTotal;
		this.fileFinish = fileFinish;
		this.length = length;
		this.finished = finished;
		isDownLoading = true;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public String getFileName()
	{
		return fileName;
	}
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	public int getLength()
	{
		return length;
	}
	public void setLength(int length)
	{
		this.length = length;
	}
	public int getFinished()
	{
		return finished;
	}
	public void setFinished(int finished)
	{
		this.finished = finished;
	}
	
	public String getFileTotal() {
		return fileTotal;
	}
	public void setFileTotal(String fileTotal) {
		this.fileTotal = fileTotal;
	}
	public String getFileFinish() {
		return fileFinish;
	}
	public void setFileFinish(String fileFinish) {
		this.fileFinish = fileFinish;
	}
	public boolean isHaved() {
		return isHaved;
	}
	public void setHaved(boolean isHaved) {
		this.isHaved = isHaved;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isDownLoading() {
		return isDownLoading;
	}
	public void setDownLoading(boolean isDownLoading) {
		this.isDownLoading = isDownLoading;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	@Override
	public String toString()
	{
		return "FileInfo [id = "+id+",url=" + url + ", fileName=" + fileName
				+ ", length=" + length + ", finished=" + finished + "]";
	}
	
}
