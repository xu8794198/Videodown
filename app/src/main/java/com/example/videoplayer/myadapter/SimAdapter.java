package com.example.videoplayer.myadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.videoplayer.R;

/**
 * Created by BXKJ on 2016-12-12.
 */
public class SimAdapter extends BaseAdapter {
    private String[] str;
    private Context context;
    private LayoutInflater inflater;
    public SimAdapter(Context context,String[] str){
        this.context = context;
        this.str = str;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return str.length;
    }

    @Override
    public Object getItem(int position) {
        return str[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_h_list, null);
            viewHolder.itemText = (TextView) convertView.findViewById(R.id.item_text);
            viewHolder.itemText2 = (TextView) convertView.findViewById(R.id.item_text2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.itemText.setText("小羊肖恩");
        viewHolder.itemText2.setText(str[position]);
        return convertView;
    }
    class ViewHolder {
        TextView itemText,itemText2;
    }
}
