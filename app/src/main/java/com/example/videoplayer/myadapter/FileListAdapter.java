package com.example.videoplayer.myadapter;

import java.math.BigDecimal;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.videoplayer.R;
import com.example.videoplayer.db.FileInfo;
import com.example.videoplayer.service.DownloadService;

/**
 *@Author: zhaocd
 *2016-5-9下午1:00:53
 *description:
 */
public class FileListAdapter extends BaseAdapter {

	private Context mContext;
	private List<FileInfo> mList;
	private static FileListAdapter myAdapter;

	private FileListAdapter(Context context, List<FileInfo> fileInfos)
	{
		this.mContext = context;
		this.mList = fileInfos;
	}
	public static FileListAdapter getInstance(Context context, List<FileInfo> fileInfos){
		if(myAdapter == null && fileInfos==null){
			myAdapter = new FileListAdapter(context, fileInfos);
		}else if(fileInfos != null){
			myAdapter = new FileListAdapter(context, fileInfos);
		}
		return myAdapter;
	}
	public void switchState(View view,boolean flag){
		if(flag){//显示 百分比，已下载MB 总MB
			view.findViewById(R.id.rate).setVisibility(View.VISIBLE);
			view.findViewById(R.id.total).setVisibility(View.VISIBLE);
			view.findViewById(R.id.fengge).setVisibility(View.VISIBLE);
			view.findViewById(R.id.haved).setVisibility(View.VISIBLE);
			view.findViewById(R.id.download_btn).setBackgroundResource(R.mipmap.pause);
		}else{//隐藏 百分比，已下载MB 总MB
			view.findViewById(R.id.rate).setVisibility(View.VISIBLE);
			view.findViewById(R.id.total).setVisibility(View.VISIBLE);
			view.findViewById(R.id.fengge).setVisibility(View.VISIBLE);
			view.findViewById(R.id.haved).setVisibility(View.VISIBLE);
			view.findViewById(R.id.download_btn).setBackgroundResource(R.mipmap.download);
		}
	}

	@Override
	public int getCount()
	{
		if(mList != null){
			return mList.size();
		}
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder = null;
		final FileInfo fileInfo = mList.get(position);
		if(100==fileInfo.getFinished()){
			fileInfo.setHaved(true);
		}
		if (convertView != null)
		{
			viewHolder = (ViewHolder) convertView.getTag();

			if (!viewHolder.mFileName.getTag().equals(fileInfo.getUrl()))
			{
				convertView = null;
			}
		}

		if (null == convertView)
		{
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.item_download, null);
			final View cView = convertView;
			viewHolder = new ViewHolder(
					(TextView) convertView.findViewById(R.id.tv_fileName),
					(TextView) convertView.findViewById(R.id.rate),
					(TextView) convertView.findViewById(R.id.haved),
					(TextView) convertView.findViewById(R.id.fengge),
					(TextView) convertView.findViewById(R.id.total),
					(ProgressBar) convertView.findViewById(R.id.pb_progress),
					(ImageView) convertView.findViewById(R.id.download_btn),
					(LinearLayout) convertView.findViewById(R.id.line_download)
			);

			viewHolder.mFileName.setText(fileInfo.getFileName());
			viewHolder.mProgressBar.setMax(100);
			convertView.setTag(viewHolder);
			viewHolder.linear.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if(fileInfo.isHaved()){
						/*Intent intent = ClassifySeeVedioActivity.getIntent(mContext, fileInfo.getId(),DownloadService.DOWNLOAD_PATH+fileInfo.getFileName() , fileInfo.getFileName(), "  s");
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						mContext.startActivity(intent);*/
					}
					else if(!fileInfo.isDownLoading()){
						// 通知Service�?始下�?
						fileInfo.setDownLoading(true);
						switchState(cView,true);
						Intent intent = new Intent(mContext.getApplicationContext(), DownloadService.class);
						intent.setAction(DownloadService.ACTION_START);
						intent.putExtra("fileInfo", fileInfo);
						mContext.startService(intent);
					}else{//暂停下载
						fileInfo.setDownLoading(false);
						switchState(cView,false);
						Intent intent = new Intent(mContext.getApplicationContext(), DownloadService.class);
						intent.setAction(DownloadService.ACTION_STOP);
						intent.putExtra("fileInfo", fileInfo);
						mContext.startService(intent);
					}
				}
			});
			viewHolder.mFileName.setTag(fileInfo.getUrl());
		}

		viewHolder.mProgressBar.setProgress(fileInfo.getFinished());
		viewHolder.rate.setText(fileInfo.getFinished()+"%");
		viewHolder.haved.setText(fileInfo.getFileFinish());
		if(fileInfo.isDownLoading())
			viewHolder.fengge.setText("/");
		viewHolder.total.setText(fileInfo.getFileTotal());
		if(100 == fileInfo.getFinished()){
			viewHolder.mDownLoadBtn.setBackgroundResource(R.mipmap.ic_loaded);
		}
		return convertView;
	}

	/**
	 * 更新列表项中的进度条
	 * @param id
	 * @param progress
	 * @return void
	 */
	public void updateProgress(int progress,int fileFinish,int fileTotal,String url,String id)
	{
		if(mList == null){
			return;
		}

		for(FileInfo file : mList){
			if(url.equals(file.getUrl())){
				file.setFinished(progress);
				file.setId(id);
				file.setFileFinish(bytes2kb(fileFinish));
				file.setFileTotal(bytes2kb(fileTotal));
			}
		}
		notifyDataSetChanged();
	}

	public String bytes2kb(long bytes) {
		BigDecimal filesize = new BigDecimal(bytes);
		BigDecimal megabyte = new BigDecimal(1024 * 1024);
		float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP).floatValue();
		if (returnValue > 1)
			return (returnValue + "MB");
		BigDecimal kilobyte = new BigDecimal(1024);
		returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP).floatValue();
		return (returnValue + "KB");
	}

	private static class ViewHolder
	{
		TextView mFileName,rate,haved,fengge,total;
		ProgressBar mProgressBar;
		ImageView mDownLoadBtn;
		LinearLayout linear;

		/**
		 *@param mFileName
		 *@param mProgressBar
		 *@param mDownLoadBtn
		 */
		public ViewHolder(TextView mFileName,TextView rate,TextView haved,TextView fengge,TextView total, ProgressBar mProgressBar,
						  ImageView mDownLoadBtn,LinearLayout linear)
		{
			this.mFileName = mFileName;
			this.rate = rate;
			this.haved = haved;
			this.fengge = fengge;
			this.total = total;
			this.mProgressBar = mProgressBar;
			this.mDownLoadBtn = mDownLoadBtn;
			this.linear = linear;
		}
	}
	@Override
	public Object getItem(int position)
	{
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}
}
