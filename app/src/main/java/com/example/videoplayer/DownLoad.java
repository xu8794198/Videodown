package com.example.videoplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.videoplayer.db.DataBaseHelper;
import com.example.videoplayer.db.FileDAO;
import com.example.videoplayer.db.FileDAOImpl;
import com.example.videoplayer.entity.Msg;
import com.example.videoplayer.myadapter.DownLoadAdapter;
import com.example.videoplayer.tool.URLConst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by BXKJ on 2016-12-09.
 */
public class DownLoad extends Activity {
    private List<Msg.DramaEntity> data;
    private static ArrayList<HashMap<String,Object>> arrayList;
    private ListView lvDownload;
    private DataBaseHelper dbHelper;
    private DownLoadAdapter dLAdapter;
    private List<String> listStr = new ArrayList<String>();
    private Button btnAction;
    private ImageView imgBack;
    private FileDAO fileDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        getListData();
        init();
//        Log.d("json","data.size()="+data.size());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        dLAdapter.notifyDataSetChanged();
    }

    private void init() {
        lvDownload = (ListView) findViewById(R.id.lv_download);
        btnAction = (Button) findViewById(R.id.btn_action);
        imgBack = (ImageView) findViewById(R.id.img_back);
        arrayList = new ArrayList<HashMap<String,Object>>();
        for(int i=0;i<data.size();i++){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("name", data.get(i));
            boolean isclick = fileDAO.isExists(URLConst.URLIP+data.get(i).getItemUrl());
            map.put("boolean", isclick);//初始化为未选
            arrayList.add(map);
            if(isclick){
                listStr.add(arrayList.get(i).get("name").toString());
            }
        }//初始化数据
        btnAction.setText("开始下载(" + listStr.size() + ")");
        dLAdapter = new DownLoadAdapter(DownLoad.this, data,arrayList);
        lvDownload.setAdapter(dLAdapter);

        lvDownload.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DownLoadAdapter.ViewCache viewCache = (DownLoadAdapter.ViewCache) view.getTag();
                viewCache.checkBox.toggle();
                arrayList.get(position).put("boolean", viewCache.checkBox.isChecked());

                dLAdapter.notifyDataSetChanged();

                if (viewCache.checkBox.isChecked()) {//被选中状态
                    listStr.add(arrayList.get(position).get("name").toString());
                } else//从选中状态转化为未选中
                {
                    listStr.remove(arrayList.get(position).get("name").toString());
                }

                btnAction.setText("开始下载(" + listStr.size() + ")");
//                Msg.DramaEntity dramaEntity = (Msg.DramaEntity) list.get(position).get("name");
                Log.d("json","drama:122");
            }
        });
        imgBack.setOnClickListener(onClickListener);
        btnAction.setOnClickListener(onClickListener);

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_back:
                    finish();
                    break;
                case R.id.btn_action:
                    Intent intent = new Intent(DownLoad.this,CurrentDownLoad.class);
                    startActivity(intent);
                    break;
            }
        }
    };
    public void getListData() {
        dbHelper = new DataBaseHelper(this);
        data = dbHelper.getAllItemList();
        fileDAO = new FileDAOImpl(DownLoad.this);
    }

    public ArrayList<HashMap<String, Object>> getList() {
        return arrayList;
    }
}
