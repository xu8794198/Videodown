package com.example.videoplayer.entity;

import java.util.List;

/**
 * @author LiJiarong
 *         Created by A on 2016/12/11.
 * @time 2016/12/11 14:49
 */
public class Msg {
    private String id;
    private String name;
    private String fmPic;
    private String totalCount;
    private String description;
    private String view;
    private String date;
    private String typeNames;
    private String isSc;
    private String itemNum;
    private String itemName;
    private String itemUrl;
    private List<DramaEntity> itemList;


    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getFmPic() {
        return fmPic;
    }
    public String getTotalCount() {
        return totalCount;
    }
    public String getDescription() {
        return description;
    }
    public String getView() {
        return view;
    }
    public String getDate() {
        return date;
    }
    public String getTypeNames() {
        return typeNames;
    }
    public String getIsSc(){
        return isSc;
    }
    public String getItemNum() {
        return itemNum;
    }
    public String getItemName() {
        return itemName;
    }
    public String getItemUrl() {
        return itemUrl;
    }
    public List<DramaEntity> getItemList() {
        return itemList;
    }


    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setFmPic(String fmPic) {
        this.fmPic = fmPic;
    }
    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setView(String view) {
        this.view = view;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setTypeNames(String typeNames) {
        this.typeNames = typeNames;
    }
    public void setIsSc(String isSc) {
        this.isSc = isSc;
    }
    public void setItemNum(String itemNum) {
        this.itemNum = itemNum;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }

    public void setItemList(List<DramaEntity> itemList) {
        this.itemList = itemList;
    }

    @Override
    public String toString() {
        return "msg:{ id:"+id+" name:"+name+" fmPic:"+fmPic+" totalCount:"+totalCount
                +" description:"+description+" view:" + view+" date:"+date+" typeNames:"
                +typeNames+" isSc:"+isSc+" itemNum:"+itemNum+" itemName"+itemName
                +" itemUrl:"+itemUrl+" itemList:"+itemList+"}";
    }

    public static class DramaEntity{
        private int itemNum;
        private String itemName;
        private String itemUrl;
        public DramaEntity(){}
        public DramaEntity(int itemNum, String itemName, String itemUrl){
                this.itemNum = itemNum;
                this.itemName = itemName;
                this.itemUrl = itemUrl;
        }
        public String getItemName() {
            return itemName;
        }
        public int getItemNum() {
            return itemNum;
        }
        public String getItemUrl() {
            return itemUrl;
        }
        public void setItemName(String itemName) {
            this.itemName = itemName;
        }
        public void setItemNum(int itemNum) {
            this.itemNum = itemNum;
        }
        public void setItemUrl(String itemUrl) {
            this.itemUrl = itemUrl;
        }

        @Override
        public String toString() {
            return "itemList:[{ itemNum:"+itemNum+" itemName:"+itemName+" itemUrl:"+itemUrl+"}]";
        }
    }
}
