package com.mentality.yun.dao;

import com.mentality.yun.domain.User;

public interface UserDao {
    /**
     * 通过用户名和密码查询数据库，存在则将信息封装并返回
     *
     * @param username
     * @param password
     * @return
     */
    User findUserByUsernameAndPassword(String username, String password);

    /**
     * 通过用户名查询用户信息
     * 存在 则将用户信息封装并返回
     * 不存在  返回空
     *
     * @param username
     * @return
     */
    User findUserByUsername(String username);

    /**
     * 存储用户信息，用于用户注册
     *
     * @param registUser
     */
    void save(User registUser);

    /**
     * 通过激活码查找用户信息
     *
     * @param code
     * @return
     */
    User findUserByCode(String code);

    /**
     * 更改用户激活状态
     *
     * @param user
     */
    void updateStatus(User user);

    /**
     * 通过Uid查询用户
     * @param uid 用户id
     * @return 对应id的用户
     */
    User findUserByUid(int uid);

    /**
     * 开通vip权限
     * @param uid
     * @return
     */
    boolean becomeVip(int uid);
}
