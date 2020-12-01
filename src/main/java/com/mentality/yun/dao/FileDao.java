package com.mentality.yun.dao;

import com.mentality.yun.domain.UserFile;

import java.util.List;

public interface FileDao {
    /**
     * 将文件的信息存入数据库中
     *
     * @param userFile
     * @return
     */
    boolean save(UserFile userFile);

    /**
     * 获取对应用户的对应文件集合
     *
     * @param uid uid，用户的编号
     * @param cid cid：
     *            1. 图片
     *            2. 音频
     *            3. 视频
     *            4. 文档
     *            5. 其他
     * @return
     */
    List<UserFile> findAllByCid(int uid, int cid);

    /**
     * 获取当前用户的所有文件对象集合
     *
     * @param uid
     * @return
     */
    List<UserFile> findAll(int uid);

    /**
     * 通过fids数组，查询所有对应的文件，并封装为用户文件数组
     *
     * @return 用户文件数组
     */
    UserFile findByFid(int fid);

    /**
     * 通过用户id 以及 文件名查询用户文件
     * @param uid 用户id
     * @param name 文件名
     * @return 查询的对应文件
     */
    UserFile findByUidAndFilename(int uid, String name);

    /**
     * 删除对应fid的文件
     * @param fid 文件id
     */
    void delByFid(int fid);
}
