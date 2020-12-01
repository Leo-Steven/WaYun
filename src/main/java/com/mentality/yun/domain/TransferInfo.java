package com.mentality.yun.domain;/*
 * @author:一身都是月~
 * @date：2020/11/20 7:58
 * */

import java.io.File;

public class TransferInfo {
    private long startIndex;
    private long endIndex;
    private File oldFile;
    private File newFile;
    private boolean isTransfer;
    private boolean isFinished;
    private boolean isDownload;
    private int taskId;
    private long count = 0L;

    public TransferInfo(long startIndex, long endIndex, File oldFile, File newFile, boolean isDownload) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.oldFile = oldFile;
        this.newFile = newFile;
        this.isDownload=isDownload;
        // 初始时任务正在下载中
        this.isTransfer = true;
        // 初始化时任务未未完成
        this.isFinished = false;
    }

    public boolean isDownload() {
        return isDownload;
    }

    public void setDownload(boolean download) {
        isDownload = download;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public long getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(long startIndex) {
        this.startIndex = startIndex;
    }

    public long getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(long endIndex) {
        this.endIndex = endIndex;
    }

    public File getOldFile() {
        return oldFile;
    }

    public void setOldFile(File oldFile) {
        this.oldFile = oldFile;
    }

    public File getNewFile() {
        return newFile;
    }

    public void setNewFile(File newFile) {
        this.newFile = newFile;
    }

    public boolean isTransfer() {
        return isTransfer;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public void setTransfer(boolean transfer) {
        isTransfer = transfer;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
