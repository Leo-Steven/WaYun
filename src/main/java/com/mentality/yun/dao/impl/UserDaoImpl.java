package com.mentality.yun.dao.impl;/*
 * @author:一身都是月~
 * @date：2020/11/7 17:27
 * */

import com.mentality.yun.dao.UserDao;
import com.mentality.yun.domain.User;
import com.mentality.yun.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author 一身都是月~
 * 用户管理管理模块---DAO---
 */
public class UserDaoImpl implements UserDao {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public User findUserByUsernameAndPassword(String username, String password) {
        User user = null;
        try {
            String sql = "select * from tab_user where username = ? and password = ? ;";
            user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username, password);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public User findUserByUsername(String username) {
        User user = null;
        try {
            String sql = "select * from tab_user where username = ? ;";
            user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
        } catch (DataAccessException e) {
            // e.printStackTrace();
            System.out.println("该用户名未被占用！");
        }
        return user;
    }

    @Override
    public void save(User registUser) {
        String sql = "insert into tab_user (username,password,name,birthday,gender,telephone,email,status,code)" +
                " values (?,?,?,?,?,?,?,?,?);";
        jdbcTemplate.update(sql, registUser.getUsername(), registUser.getPassword(), registUser.getName(),
                registUser.getBirthday(), registUser.getGender(), registUser.getTelephone(),
                registUser.getEmail(), registUser.getStatus(), registUser.getCode());
    }

    @Override
    public User findUserByCode(String code) {
        User user = null;
        try {
            String sql = "select * from tab_user where code = ?;";
            user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), code);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void updateStatus(User user) {
        String sql = "update tab_user set Status = 'Y' where uid = ? ;";
        jdbcTemplate.update(sql, user.getUid());
    }

    @Override
    public User findUserByUid(int uid) {
        User user = null;
        try {
            String sql = "select * from tab_user where uid = ?;";
            user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class),uid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public boolean becomeVip(int uid) {
        try {
            String sql = "update tab_user set vip = 'Y' where uid = ?;";
            jdbcTemplate.update(sql,uid);
        } catch (DataAccessException e) {

            e.printStackTrace();
            return false;
        }
        return true;
    }
}
