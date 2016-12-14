package com.example.videoplayer.tool;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author LiJiarong
 *         Created by A on 2016/12/11.
 * @time 2016/12/11 5:18
 */
public class HttpUtil {
    private String jsonString;
    public HttpUtil(){}
    public String asynGet(String url){
        final OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .build();
        final Request request = new Request.Builder()
                .url(url)
//	    .header("User-Agent","OkHttp Example")
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("json","response====="+e.getMessage());
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
//							Log.d("json", "response=====" + response.body().toString());
                            jsonString = response.body().string();
//                            Gson gson = new Gson();
//                            BaseEntity<Msg> baseEntity = gson.fromJson(jsonString,new TypeToken<BaseEntity<Msg>>() {}.getType());
//                            Log.d("json","BaseEntity "+baseEntity.getMsg());
//                            Msg msg = baseEntity.getMsg();
//                            Log.d("json","Msg "+baseEntity.getMsg().getItemUrl());
//                            List<Msg.DramaEntity> dramaEntitiesList = baseEntity.getMsg().getItemList();
//                            Log.d("json","DramaEntity "+dramaEntitiesList);
                            response.body().close();
                        }
                    }
                });
            }
        }).start();
        return jsonString;
    }

    public String syncGet(String url){
        OkHttpClient client=new OkHttpClient();
		Request request = new Request.Builder()
		        .url(url)
		        .get()
		        .build();
		try{
			Response response = client.newCall(request).execute();
		    Log.d("json", response.body().string());
		    ;
		}catch (Exception e){
			e.printStackTrace();
			Log.i("json", e.getMessage()+"/"+e.getCause());
		}
        return jsonString;
    }
}
