package com.ustb.shellbox.shelllife.databean;

/**
 * Created by 37266 on 2016/12/24.
 */
public class AccountInfo  {
    private String webName;
    private String userName;
    private String password;
    private int accountType;
    private int imgWeb;
    private int imgAccount;
    private int imgPassword;
    private int imgInfo;

    public AccountInfo(String webName, String userName, String password,int accountType, int imgWeb, int imgAccount, int imgPassword, int imgInfo) {
        this.webName = webName;
        this.userName = userName;
        this.password = password;
        this.accountType=accountType;
        this.imgWeb = imgWeb;
        this.imgAccount = imgAccount;
        this.imgPassword = imgPassword;
        this.imgInfo = imgInfo;
    }

    public String getWebName() {
        return webName;
    }

    public void setWebName(String webName) {
        this.webName = webName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public int getImgWeb() {
        return imgWeb;
    }

    public void setImgWeb(int imgWeb) {
        this.imgWeb = imgWeb;
    }

    public int getImgAccount() {
        return imgAccount;
    }

    public void setImgAccount(int imgAccount) {
        this.imgAccount = imgAccount;
    }

    public int getImgPassword() {
        return imgPassword;
    }

    public void setImgPassword(int imgPassword) {
        this.imgPassword = imgPassword;
    }

    public int getImgInfo() {
        return imgInfo;
    }

    public void setImgInfo(int imgInfo) {
        this.imgInfo = imgInfo;
    }
}
