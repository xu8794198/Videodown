package com.example.videoplayer.entity;

/**
 * Created by Administrator on 2016/9/26.
 */
public class BaseEntity<T> {
//    /**
//     * 返回文字内容
//     */
//    private String message;
    /**
     * 状态
     */
    private int status;
    /**
     * 数据(可能是集合也可能是对象)
     */
    private T msg;

    public BaseEntity() {

    }

    public T getMsg() {
        return msg;
    }

    public void setMsg(T msg) {
        this.msg = msg;
    }

//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
