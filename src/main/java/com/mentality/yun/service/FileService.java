package com.mentality.yun.service;

import com.mentality.yun.domain.UserFile;

import java.util.List;

public interface FileService {
    /**
     * 对文件信息进行存储的service方法
     *
     * @param userFile
     * @return
     */
    boolean save(UserFile userFile);

    /**
     * 获取对应文件类别id的文件对象集合
     *
     * @param uid
     * @param cid
     * @return
     */
    List<UserFile> findAllByCid(int uid, int cid);

    /**
     * 获取对应用户的所有文件
     *
     * @param uid
     * @return
     */
    List<UserFile> findAll(int uid);

    /**
     * 获取对应文件id的文件对象
     *
     * @param fid 文件id
     * @return UserFile文件对象
     */
    UserFile getPathByFid(int fid);

    /**
     * 删除 数据库中的记录
     * @param uid 用户id
     * @param name 文件名
     */
    void delByUidAndFilename(int uid, String name);

    /**
     * 删除对应的文件
     * @param fid 文件id
     */
    void delByFid(int fid);

    /**
     * 查询对应fid的文件
     * @param fid 文件id
     * @return userFile对象
     */
    UserFile findByFid(int fid);
}
