package com.mentality.yun.domain;
/*
 * @author:一身都是月~
 * @date：2020/11/13 12:22
 * */

public class UserFile {
    private String faddress;
    private int fid;
    private String fname;
    private int uid;
    private int cid;
    private String date;

    public String getFaddress() {
        return faddress;
    }

    public void setFaddress(String faddress) {
        this.faddress = faddress;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "UserFile{" +
                "faddress='" + faddress + '\'' +
                ", fid=" + fid +
                ", fname='" + fname + '\'' +
                ", uid=" + uid +
                ", date='" + date + '\'' +
                '}';
    }
}
