package com.mentality.yun.domain;

import java.io.Serializable;

/**
 * 用户实体类
 */
public class User implements Serializable {
    private int uid;//用户id
    private String username;//用户名，账号
    private String password;//密码
    private String name;//真实姓名
    private String birthday;//出生日期
    private String gender;//男或女
    private String telephone;//手机号
    private String email;//邮箱
    private String status;//激活状态，Y代表激活，N代表未激活
    private String vip;  // 是否为会员，Y表示是会员，N表示非会员
    private String code;//激活码（要求唯一）

    /**
     * 无参构造方法
     */
    public User() {
    }

    /**
     * 有参构方法
     *
     * @param uid
     * @param username
     * @param password
     * @param name
     * @param birthday
     * @param sex
     * @param telephone
     * @param email
     * @param status
     * @param code
     */
    public User(int uid, String username, String password, String name, String birthday, String sex, String telephone, String email, String status, String code) {
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.name = name;
        this.birthday = birthday;
        this.gender = sex;
        this.telephone = telephone;
        this.email = email;
        this.status = status;
        this.code = code;
    }

    public User(int uid, String username, String password, String name, String birthday, String gender, String telephone, String email, String status, String vip, String code) {
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
        this.telephone = telephone;
        this.email = email;
        this.status = status;
        this.vip = vip;
        this.code = code;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
