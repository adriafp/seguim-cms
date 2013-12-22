package com.seguim.aws.pojo;

/**
 * User: adria
 * Date: 22/12/13
 * Time: 16:42
 */
public class S3ObjectPojo {

    private boolean folder;
    private String key;
    private String realName;
    private String bucketName;

    public boolean isFolder() {
        return folder;
    }

    public void setFolder(boolean folder) {
        this.folder = folder;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
}
