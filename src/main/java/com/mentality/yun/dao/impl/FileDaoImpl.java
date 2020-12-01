package com.mentality.yun.dao.impl;/*
 * @author:一身都是月~
 * @date：2020/11/13 9:49
 * */

import com.mentality.yun.dao.FileDao;
import com.mentality.yun.domain.User;
import com.mentality.yun.domain.UserFile;
import com.mentality.yun.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class FileDaoImpl implements FileDao {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public boolean save(UserFile userFile) {
        String sql = "insert into user_file (fname,uid,cid,fdate,faddress) values (?,?,?,?,?);";
        try {
            jdbcTemplate.update(sql, userFile.getFname(), userFile.getUid(), userFile.getCid(), userFile.getDate(), userFile.getFaddress());
        } catch (DataAccessException e) {
            System.out.println("数据库存储出错~");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public List<UserFile> findAllByCid(int uid, int cid) {
        List<UserFile> list = null;
        try {
            String sql = "select * from user_file where uid = ? and cid = ?;";
            list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<UserFile>(UserFile.class), uid, cid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<UserFile> findAll(int uid) {
        List<UserFile> list = null;
        try {
            String sql = "select * from user_file where uid = ?;";
            list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<UserFile>(UserFile.class), uid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public UserFile findByFid(int fid) {
        String sql = "select * from user_file where fid = ?";
        UserFile userFile = null;
        try {
            userFile = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<UserFile>(UserFile.class), fid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return userFile;
    }

    @Override
    public UserFile findByUidAndFilename(int uid, String name) {
        UserFile userFile = null;
        try {
            String sql = "select * from user_file where uid = ? and fname = ? ;";
            userFile = jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<UserFile>(UserFile.class),uid,name);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return userFile;
    }

    @Override
    public void delByFid(int fid) {
        try {
            String sql = "delete from user_file where fid = ?;";
            jdbcTemplate.update(sql,fid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}
