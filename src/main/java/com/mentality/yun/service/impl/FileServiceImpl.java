package com.mentality.yun.service.impl;/*
 * @author:一身都是月~
 * @date：2020/11/13 9:48
 * */

import com.mentality.yun.dao.FileDao;
import com.mentality.yun.dao.impl.FileDaoImpl;
import com.mentality.yun.domain.User;
import com.mentality.yun.domain.UserFile;
import com.mentality.yun.service.FileService;

import java.util.List;

public class FileServiceImpl implements FileService {
    private FileDao fileDao = new FileDaoImpl();

    @Override
    public boolean save(UserFile userFile) {
        boolean flag = fileDao.save(userFile);
        return flag;
    }

    @Override
    public List<UserFile> findAllByCid(int uid, int cid) {
        List<UserFile> userFiles = fileDao.findAllByCid(uid, cid);
        return userFiles;
    }

    @Override
    public List<UserFile> findAll(int uid) {
        List<UserFile> list = fileDao.findAll(uid);
        return list;
    }

    @Override
    public UserFile getPathByFid(int fid) {
        UserFile userFile = fileDao.findByFid(fid);
        return userFile;
    }

    @Override
    public void delByUidAndFilename(int uid, String name) {
        UserFile userFile = fileDao.findByUidAndFilename(uid,name);
        fileDao.delByFid(userFile.getFid());
    }

    @Override
    public void delByFid(int fid) {
        fileDao.delByFid(fid);
    }

    @Override
    public UserFile findByFid(int fid) {
        UserFile userFile = fileDao.findByFid(fid);
        return userFile;
    }
}
