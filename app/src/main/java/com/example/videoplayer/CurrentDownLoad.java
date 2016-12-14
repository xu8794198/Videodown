package com.example.videoplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.videoplayer.db.DBHelper;
import com.example.videoplayer.db.FileDAOImpl;
import com.example.videoplayer.db.FileInfo;
import com.example.videoplayer.db.ThreadDAO;
import com.example.videoplayer.db.ThreadDAOImpl;
import com.example.videoplayer.db.ThreadInfo;
import com.example.videoplayer.entity.Msg;
import com.example.videoplayer.myadapter.FileListAdapter;
import com.example.videoplayer.service.DownloadService;
import com.example.videoplayer.tool.URLConst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by BXKJ on 2016-12-12.
 */
public class CurrentDownLoad extends Activity {
    private ListView mListView = null;
    private List<FileInfo> mFileInfoList;
    private FileListAdapter mAdapter = null;
    private ThreadDAO mDao = null;
    private long exitTime;
    private FileDAOImpl fileDao = null;
    private DownloadService filedownService;
    private Button btnAction;
    private ImageView imgBack;
    private ArrayList<HashMap<String,Object>> list;
    private Msg.DramaEntity dramaEntity;
    private TextView noDownData;
    private FileInfo fileInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_download_list);
        getDataes();
        init();

        mListView = (ListView) findViewById(R.id.lv_downLoad);
        mFileInfoList = new ArrayList<FileInfo>();

        mDao = new ThreadDAOImpl(this);
        fileDao = new FileDAOImpl(this);
        int j=0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).get("boolean").toString().equals("true")) {
                dramaEntity = (Msg.DramaEntity) list.get(i).get("name");
                FileInfo fileInfo = new FileInfo(String.valueOf(j), URLConst.URLIP + dramaEntity.getItemUrl(),
                        "小羊肖恩" + dramaEntity.getItemName() + ".mp4");
                fileInfo.setDownLoading(false);
                FileInfo file = fileDao.getFiles(fileInfo.getUrl());
                if (file != null) {
                    fileInfo.setFinished(file.getFinished());
                }
                mFileInfoList.add(fileInfo);
                j++;
            }
        }
        if(j == 0) {
            noDownData.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }
//        FileInfo fileInfo1 = new FileInfo("1", "http://www.apk3.com/uploads/soft/201603/huangshizhanzhen.apk", "huangshizhanzhen.apk");
//
//        fileInfo1.setDownLoading(false);
//        FileInfo file1 = fileDao.getFiles(fileInfo1.getUrl());
//        if(file1 != null){
//            fileInfo1.setFinished(file1.getFinished());
//        }
//        mFileInfoList.add(fileInfo1);

        mAdapter = FileListAdapter.getInstance(this, mFileInfoList);
        mListView.setAdapter(mAdapter);

    }

    private void init() {
        btnAction = (Button) findViewById(R.id.btn_action);
        btnAction.setVisibility(View.GONE);
        noDownData = (TextView) findViewById(R.id.noDownData);
        imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CurrentDownLoad.this.getApplicationContext(), DownloadService.class);
                stopService(intent);
                filedownService = null;
                finish();
            }
        });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Intent intent = new Intent(CurrentDownLoad.this.getApplicationContext(), DownloadService.class);
        stopService(intent);
        filedownService = null;
        return super.onKeyDown(keyCode, event);
    }

    private void getDataes() {
        DownLoad down = new DownLoad();
        list = down.getList();
    }
}
