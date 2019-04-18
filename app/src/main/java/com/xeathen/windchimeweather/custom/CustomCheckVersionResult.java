package com.xeathen.windchimeweather.custom;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author: 蛋清蛋黄
 * @date: 2019/4/9
 * @description:
 * 版本更新检查返回的结果
 *
 * 0:无版本更新
 * 1:有版本更新，不需要强制升级
 * 2:有版本更新，需要强制升级
 */
public class CustomCheckVersionResult implements Serializable{
    /**
     * 无版本更新
     */
    public final static int NO_NEW_VERSION = 0; // 0:无版本更新
    /**
     * 有版本更新，不需要强制升级
     */
    public final static int HAVE_NEW_VERSION = 1; // 1:有版本更新，不需要强制升级
    /**
     * 有版本更新，需要强制升级
     */
    public final static int HAVE_NEW_VERSION_FORCED_UPLOAD = 2; // 2:有版本更新，需要强制升级

    /**
     * 请求返回码
     */
    @SerializedName("Code")
    private int code;

    /**
     * 请求信息
     */
    @SerializedName("Msg")

    private String msg;

    @SerializedName("Data")
    private Data data;



    public int getCode() {
        return code;
    }

    public CustomCheckVersionResult setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public CustomCheckVersionResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public int getUpdateStatus() {
        return data.updateStatus;
    }

    public CustomCheckVersionResult setRequireUpgrade(int updateStatus) {
        data.updateStatus = updateStatus;
        return this;
    }

    public String getUploadTime() {
        return data.uploadTime;
    }

    public CustomCheckVersionResult setUploadTime(String uploadTime) {
        data.uploadTime = uploadTime;
        return this;
    }

    public int getVersionCode() {
        return data.versionCode;
    }

    public CustomCheckVersionResult setVersionCode(int versionCode) {
        data.versionCode = versionCode;
        return this;
    }

    public String getVersionName() {
        return data.versionName;
    }

    public CustomCheckVersionResult setVersionName(String versionName) {
        data.versionName = versionName;
        return this;
    }

    public String getModifyContent() {
        return data.modifyContent;
    }

    public CustomCheckVersionResult setModifyContent(String modifyContent) {
        data.modifyContent = modifyContent;
        return this;
    }

    public String getDownloadUrl() {
        return data.downloadUrl;
    }

    public CustomCheckVersionResult setDownloadUrl(String downloadUrl) {
        data.downloadUrl = downloadUrl;
        return this;
    }

    public String getApkMd5() {
        return data.apkMd5;
    }

    public CustomCheckVersionResult setApkMd5(String apkMd5) {
        data.apkMd5 = apkMd5;
        return this;
    }

    public long getApkSize() {
        return data.apkSize;
    }

    public CustomCheckVersionResult setApkSize(long apkSize) {
        data.apkSize = apkSize;
        return this;
    }

    @Override
    public String toString() {
        return "CheckVersionResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", UpdateStatus=" + data.updateStatus +
                ", VersionCode=" + data.versionCode +
                ", VersionName='" + data.versionName + '\'' +
                ", UploadTime='" + data.uploadTime + '\'' +
                ", ModifyContent='" + data.modifyContent + '\'' +
                ", DownloadUrl='" + data.downloadUrl + '\'' +
                ", ApkMd5='" + data.apkMd5 + '\'' +
                ", ApkSize=" + data.apkSize+
                '}';
    }

    public class Data{
        /**
         * 更新的状态
         */
        public int updateStatus;
        /**
         * 最新版本号[根据版本号来判别是否需要升级]
         */
        public int versionCode;
        /**
         * 最新APP版本的名称[用于展示的版本名]
         */
        public String versionName;
        /**
         * APP更新时间
         */
        public String uploadTime;
        /**
         * APP变更的内容
         */
        public String modifyContent;
        /**
         * 下载地址
         */
        public String downloadUrl;
        /**
         * Apk MD5值
         */
        private String apkMd5;
        /**
         * Apk大小【单位：KB】
         */
        private long apkSize;
    }
}
