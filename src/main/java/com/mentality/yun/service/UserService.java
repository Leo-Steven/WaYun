package com.mentality.yun.service;

import com.mentality.yun.domain.User;

public interface UserService {
    /**
     * 登录功能
     *
     * @param loginUser
     * @return
     */
    User login(User loginUser);

    /**
     * 用户注册
     *
     * @param registUser
     * @return
     */
    boolean regist(User registUser);

    /**
     * 通过传入的激活码进行用户激活
     * 1. 激活成功返回true
     * 2. 激活失败返回false
     *
     * @param code
     * @return
     */
    boolean active(String code);

    /**
     * 查询是否存在该username的用户
     *
     * @param username
     * @return
     */
    boolean findByUsername(String username);

    /**
     * 判断用户是否是vip
     * @param uid 用户id
     * @return true---是    false---否
     */
    boolean isVip(int uid);

    /**
     * 给对应用户开通vip权限
     * @param uid
     * @return
     */
    boolean becomeVip(int uid);

    /**
     * 更新当前用户的session信息
     * @param uid
     * @return
     */
    Object updateUserInfo(int uid);
}
