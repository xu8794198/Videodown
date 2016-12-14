package com.example.videoplayer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.videoplay.part.SimpleVideoView;
import com.example.videoplayer.db.DataBaseHelper;
import com.example.videoplayer.entity.BaseEntity;
import com.example.videoplayer.entity.Msg;
import com.example.videoplayer.myadapter.SimAdapter;
import com.example.videoplayer.tool.URLConst;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private SimpleVideoView simpleVideoView;
    private LinearLayout linearLayoutDownLoad;
    private TextView txtName,txtDescription;
    private String name,description,itemUrl;
    private ListView listView;
    private SimAdapter adapter;
    private String[] strings;
    private DataBaseHelper dataBaseHelper;
    private ScrollView scrollView;
    private String currentUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        getRequest();
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Toast.makeText(MainActivity.this,"onFailure",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(MainActivity.this,"Request success!",Toast.LENGTH_SHORT).show();
                    txtName.setText(name);
                    txtDescription.setText(description);
                    adapter = new SimAdapter(MainActivity.this,strings);
                    listView.setAdapter(adapter);
                    scrollView.smoothScrollTo(0,0);
                    break;
                case 2:
                    Toast.makeText(MainActivity.this,"请求失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    public void onContentChanged() {
        super.onContentChanged();

    }
    private ListView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            dataBaseHelper = new DataBaseHelper(MainActivity.this);
//            Log.d("json", "url:" + URLConst.URLIP + dataBaseHelper.getUrl(position + 1));
            String videoPath = URLConst.URLIP+dataBaseHelper.getUrl(position+1);
            if(currentUrl.equals(videoPath)){
                return;
            }
            currentUrl=videoPath;
            simpleVideoView.onPause();
            simpleVideoView.onResume();
            simpleVideoView.setVideoPath(videoPath);

        }
    };
    private void init() {
        simpleVideoView = (SimpleVideoView) findViewById(R.id.simpleVideoPlayer);
        linearLayoutDownLoad = (LinearLayout) findViewById(R.id.ll_download);
        txtName = (TextView) findViewById(R.id.txt_name);
        txtDescription = (TextView) findViewById(R.id.txt_description);
        scrollView = (ScrollView) findViewById(R.id.scrollView1);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(onItemClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        simpleVideoView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        simpleVideoView.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_download:
                Intent intent = new Intent(MainActivity.this,DownLoad.class);
                startActivity(intent);
            break;
        }
    }
    private void getRequest() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URLConst.URLMOVIEINFORMATION) //携带参数
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("tag","onFailure");
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                setResult(response.body().string());
            }
        });

        linearLayoutDownLoad.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        // 设置预览图
//        String previewPath = URLConst.URLIP+itemUrl;
//        Picasso.with(getBaseContext()).load(previewPath).into(simpleVideoView.getPreviewView());
        // 设置播放源
        currentUrl=URLConst.URLIP+"/upload/media/N86Z642510_807631.mp4";
//        Log.d("json","videoPath:"+currentUrl);
        simpleVideoView.setVideoPath(currentUrl);
    }

    private void setResult(String string) {
        if(string==null|| TextUtils.isEmpty(string)){
            handler.sendEmptyMessage(2);
            //Toast.makeText(MainActivity.this,"request is empty",Toast.LENGTH_SHORT).show();
            return;
        }
        Gson gson=new Gson();
        BaseEntity<Msg> baseEntity = gson.fromJson(string,new TypeToken<BaseEntity<Msg>>() {}.getType());
        if(baseEntity.getStatus()==10001){
            Msg msg = baseEntity.getMsg();
            name = msg.getName();
            description = msg.getDescription();
            itemUrl = msg.getItemUrl();
            List<Msg.DramaEntity> dramaEntityList = baseEntity.getMsg().getItemList();
            strings=new String[dramaEntityList.size()];
            int i = 0;
            dataBaseHelper= new DataBaseHelper(this);

            for (Msg.DramaEntity list:dramaEntityList){
                dataBaseHelper.deleteItemList(list);
                dataBaseHelper.addItemList(list);
                strings[i] = list.getItemName();
                i++;
            }
            handler.sendEmptyMessage(1);
        }else if(baseEntity.getStatus()==30001){
            handler.sendEmptyMessage(2);
        }else if(baseEntity.getStatus()==30002){
            handler.sendEmptyMessage(2);
        }else if(baseEntity.getStatus()==30003){
            handler.sendEmptyMessage(2);
        }else if(baseEntity.getStatus()==30004){
            handler.sendEmptyMessage(2);
        }else if(baseEntity.getStatus()==30005){
            handler.sendEmptyMessage(2);
        }
//        Log.d("json","BaseEntity "+baseEntity.getMsg());

//        Log.d("json","Msg "+baseEntity.getMsg().getItemUrl());
//        Log.d("json","getOneItemUrl"+animationEntity.msgEntity.getOneItemUrl());
//        List<Msg.DramaEntity> dramaEntitiesList = baseEntity.getMsg().getItemList();
//        Log.d("json","DramaEntity "+dramaEntitiesList);
    }

}
