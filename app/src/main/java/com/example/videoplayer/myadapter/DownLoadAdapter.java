package com.example.videoplayer.myadapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.videoplayer.R;
import com.example.videoplayer.db.DBHelper;
import com.example.videoplayer.db.FileDAO;
import com.example.videoplayer.db.FileDAOImpl;
import com.example.videoplayer.db.FileInfo;
import com.example.videoplayer.entity.Msg;
import com.example.videoplayer.tool.URLConst;

import java.util.HashMap;
import java.util.List;

/**
 * Created by BXKJ on 2016-12-12.
 */
public class DownLoadAdapter extends BaseAdapter {
    private List<Msg.DramaEntity> str;
    private List<HashMap<String,Object>> list;
    private Context context;
    private LayoutInflater inflater;
    private FileInfo fileInfo;
    private FileDAO fileDAO;
    public DownLoadAdapter(Context context,List<Msg.DramaEntity> data,List<HashMap<String,Object>> list){
        this.context = context;
        this.str = data;
        this.list = list;
        inflater = LayoutInflater.from(context);
        fileDAO = new FileDAOImpl(context);

    }

    @Override
    public int getCount() {
        return str.size();
    }

    @Override
    public Object getItem(int position) {
        return str.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewCache viewCache = null;
        if(convertView == null) {
            viewCache = new ViewCache();
            convertView = inflater.inflate(R.layout.item_download_list, null);
            viewCache.itemText = (TextView) convertView.findViewById(R.id.item_text);
            viewCache.itemText2 = (TextView) convertView.findViewById(R.id.item_text2);
            viewCache.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            viewCache.txtCurDoenload = (TextView) convertView.findViewById(R.id.txt_cur_doenload);
            convertView.setTag(viewCache);
        } else {
            viewCache = (ViewCache) convertView.getTag();
        }
        viewCache.itemText.setText("小羊肖恩");
        viewCache.itemText2.setText(str.get(position).getItemName());
        if(!isEnabled(position)){
            viewCache.checkBox.setChecked(true);
            viewCache.txtCurDoenload.setVisibility(View.VISIBLE);
            viewCache.checkBox.setVisibility(View.GONE);
            return convertView;
        }
        viewCache.checkBox.setChecked((Boolean) list.get(position).get("boolean"));
        viewCache.txtCurDoenload.setVisibility(View.GONE);
        viewCache.checkBox.setVisibility(View.VISIBLE);
        return convertView;
    }
    public class ViewCache {
        TextView itemText,itemText2,txtCurDoenload;
        public CheckBox checkBox;
    }

    @Override
    public boolean isEnabled(int position) {
        String url= URLConst.URLIP+str.get(position).getItemUrl();
//        Log.d("json","strUrl:"+str.get(position).getItemUrl());
        return !fileDAO.isExists(url);
    }
}
