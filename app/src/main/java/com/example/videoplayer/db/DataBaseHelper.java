package com.example.videoplayer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.videoplayer.entity.Msg;

import java.util.ArrayList;
import java.util.List;

import io.vov.vitamio.utils.Log;

/**
 * @author LiJiarong
 *         Created by A on 2016/8/11.
 * @time 2016/8/11 14:59
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DBName = "customer.db";
    private static final String TABLE_NAME = "itemList";
    private static final int Version = 1;
    private static final String ITEMNUM="itemNum";
    private static final String ITEMNAME="itemName";
    private static final String ITEMURL="itemUrl";

    public DataBaseHelper(Context context) {
        super(context, DBName, null, Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (itemNum INTEGER, " +
                "itemName VARCHAR, itemUrl VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exists "+TABLE_NAME;
        db.execSQL(sql);
        this.onCreate(db);
    }
    public void addItemList(Msg.DramaEntity dramaEntity){
        SQLiteDatabase db=this.getWritableDatabase();
        //使用ContentValues添加数据
        ContentValues values=new ContentValues();
        values.put(ITEMNUM,dramaEntity.getItemNum());
        values.put(ITEMNAME,dramaEntity.getItemName());
        values.put(ITEMURL,dramaEntity.getItemUrl());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public Msg.DramaEntity getItemList(String name){
        SQLiteDatabase db=this.getWritableDatabase();

        //Cursor对象返回查询结果
        Cursor cursor=db.query(TABLE_NAME,
                new String[]{ITEMNUM,ITEMNAME,ITEMURL},
                ITEMNAME+"=?",new String[]{name},null,null,null,null);

        Msg.DramaEntity dramaEntity=null;
        //注意返回结果有可能为空
        if(cursor.moveToFirst()){
            dramaEntity=new Msg.DramaEntity(cursor.getInt(0),cursor.getString(1), cursor.getString(2));
        }
        cursor.close();
        db.close();
        return dramaEntity;
    }
    public int getItemListCounts(){
        String selectQuery="SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);
        cursor.close();
        db.close();
        return cursor.getCount();
    }

    public String getUrl(int itemnum){
        String selectQuery = "SELECT * FROM "+TABLE_NAME +" WHERE "+ITEMNUM+"=" + itemnum;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String url = cursor.getString(2);
        cursor.close();
        return url;
    }
    //查找所有DramaEntity
    public List<Msg.DramaEntity> getAllItemList(){
        List<Msg.DramaEntity> dramaEntityList=new ArrayList<Msg.DramaEntity>();

        String selectQuery="SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do{
                Msg.DramaEntity dramaEntity=new Msg.DramaEntity();
                dramaEntity.setItemNum(Integer.parseInt(cursor.getString(0)));
                dramaEntity.setItemName(cursor.getString(1));
                dramaEntity.setItemUrl(cursor.getString(2));
                dramaEntityList.add(dramaEntity);
            }while(cursor.moveToNext());
        }
        return dramaEntityList;
    }
    public List<String> getAllItemName(){
        List<String> str = new ArrayList<>();
        String selectQuery="SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do{
                str.add(cursor.getString(cursor.getColumnIndex(ITEMNAME)));
                Log.d("json","cursor: "+cursor.getString(cursor.getColumnIndex(ITEMNAME)));
            }while (cursor.moveToNext());
        }
        return str;
    }

    //更新ItemList
    public int updateItemList(Msg.DramaEntity dramaEntity){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(ITEMNAME,dramaEntity.getItemName());
        values.put(ITEMURL,dramaEntity.getItemUrl());

        return db.update(TABLE_NAME,values,ITEMNUM+"=?",new String[]{String.valueOf(dramaEntity.getItemNum())});
    }
    public void deleteItemList(Msg.DramaEntity dramaEntity){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME,ITEMNAME+"=?",new String[]{dramaEntity.getItemName()});
        db.close();
    }
}
