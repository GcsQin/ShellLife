package com.ustb.shellbox.shelllife.databean;

import java.io.Serializable;

/**
 * Created by 37266 on 2017/3/17.
 */
public class ZhiyuanInfo implements Serializable{
    private String actName;
    private String actTime;
    private String planRenshu;
    private String joinRenshu;
    private String actDeadline;
    private String actNum;
    private String actLocation;
    private String actGongshi;
    private String actIntroduce;
    private String actDuty;
    private String actFinalGongshi;
    private String actResult;

    public ZhiyuanInfo(String actName, String actTime, String actDeadline, String planRenshu, String joinRenshu, String actNum, String actLocation, String actGongshi, String actDuty, String actFinalGongshi, String actIntroduce, String actResult) {
        this.actName = actName;
        this.actTime = actTime;
        this.actDeadline = actDeadline;
        this.planRenshu = planRenshu;
        this.joinRenshu = joinRenshu;
        this.actNum = actNum;
        this.actLocation = actLocation;
        this.actGongshi = actGongshi;
        this.actDuty = actDuty;
        this.actFinalGongshi = actFinalGongshi;
        this.actIntroduce = actIntroduce;
        this.actResult = actResult;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public String getActTime() {
        return actTime;
    }

    public void setActTime(String actTime) {
        this.actTime = actTime;
    }

    public String getActDeadline() {
        return actDeadline;
    }

    public void setActDeadline(String actDeadline) {
        this.actDeadline = actDeadline;
    }

    public String getPlanRenshu() {
        return planRenshu;
    }

    public void setPlanRenshu(String planRenshu) {
        this.planRenshu = planRenshu;
    }

    public String getJoinRenshu() {
        return joinRenshu;
    }

    public void setJoinRenshu(String joinRenshu) {
        this.joinRenshu = joinRenshu;
    }

    public String getActNum() {
        return actNum;
    }

    public void setActNum(String actNum) {
        this.actNum = actNum;
    }

    public String getActLocation() {
        return actLocation;
    }

    public void setActLocation(String actLocation) {
        this.actLocation = actLocation;
    }

    public String getActGongshi() {
        return actGongshi;
    }

    public void setActGongshi(String actGongshi) {
        this.actGongshi = actGongshi;
    }

    public String getActDuty() {
        return actDuty;
    }

    public void setActDuty(String actDuty) {
        this.actDuty = actDuty;
    }

    public String getActFinalGongshi() {
        return actFinalGongshi;
    }

    public void setActFinalGongshi(String actFinalGongshi) {
        this.actFinalGongshi = actFinalGongshi;
    }

    public String getActIntroduce() {
        return actIntroduce;
    }

    public void setActIntroduce(String actIntroduce) {
        this.actIntroduce = actIntroduce;
    }

    public String getActResult() {
        return actResult;
    }

    public void setActResult(String actResult) {
        this.actResult = actResult;
    }
}
