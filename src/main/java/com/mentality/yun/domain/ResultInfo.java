package com.mentality.yun.domain;/*
 * @author:一身都是月~
 * @date：2020/10/31 14:49
 * */


import java.io.Serializable;

public class ResultInfo implements Serializable {
    private static final long serialVersionUID = -7195983228797788055L;
    private boolean flag;
    // 后端返回给前端的数据对象
    private Object data;
    private String errorMsg;

    public ResultInfo() {
    }

    public ResultInfo(boolean flag, Object data) {
        this.flag = flag;
        this.data = data;
    }

    public ResultInfo(boolean flag, Object data, String errorMsg) {
        this.flag = flag;
        this.data = data;
        this.errorMsg = errorMsg;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
