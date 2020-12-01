package com.mentality.yun.service.impl;/*
 * @author:一身都是月~
 * @date：2020/11/7 17:28
 * */

import com.mentality.yun.dao.UserDao;
import com.mentality.yun.dao.impl.UserDaoImpl;
import com.mentality.yun.domain.User;
import com.mentality.yun.service.UserService;

public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();

    @Override
    public User login(User loginUser) {
        User user = null;
        // 1. 通过账号密码查询数据库，并返回User对象
        user = userDao.findUserByUsernameAndPassword(loginUser.getUsername(), loginUser.getPassword());
        return user;
    }

    @Override
    public boolean regist(User registUser) {
        userDao.save(registUser);
        return true;
    }

    @Override
    public boolean active(String code) {
        User user = null;
        // 1. 通过激活码查询用户信息
        user = userDao.findUserByCode(code);
        // 2. 判断是否存在对应用户信息
        if (user != null) {
            // 2.1 存在
            userDao.updateStatus(user);
            return true;
        }
        // 2.2 不存在
        return false;
    }

    @Override
    public boolean findByUsername(String username) {
        User user = userDao.findUserByUsername(username);
        if (user == null) {
            // 不存在该username
            return false;
        } else {
            // 存在该username
            return true;
        }
    }

    @Override
    public boolean isVip(int uid) {
        User user = userDao.findUserByUid(uid);
        if (user!=null&& "Y".equals(user.getVip())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean becomeVip(int uid) {
        boolean flag = userDao.becomeVip(uid);
        return flag;
    }

    @Override
    public Object updateUserInfo(int uid) {
        User user = null;
        user = userDao.findUserByUid(uid);
        return user;
    }
}
