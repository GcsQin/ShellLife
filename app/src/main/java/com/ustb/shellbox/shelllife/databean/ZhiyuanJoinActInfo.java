package com.ustb.shellbox.shelllife.databean;

/**
 * Created by 37266 on 2017/3/17.
 */
public class ZhiyuanJoinActInfo  {
    private String joinActName;
    private String joinActTime;
    private String joinNumOfPlan;
    private String joinNumOfJoin;
    private String joinActNum;
    private String joinActType;
    private String joinActLocation;
    private String joinActGongshi;
    private String joinActDeadline;
    private String joinActDetials;
    private String joinActDuty;

    public ZhiyuanJoinActInfo(String joinActName, String joinActTime, String joinNumOfPlan, String joinNumOfJoin, String joinActNum, String joinActType, String joinActLocation, String joinActGongshi, String joinActDeadline, String joinActDetials, String joinActDuty) {
        this.joinActName = joinActName;
        this.joinActTime = joinActTime;
        this.joinNumOfPlan = joinNumOfPlan;
        this.joinNumOfJoin = joinNumOfJoin;
        this.joinActNum = joinActNum;
        this.joinActType = joinActType;
        this.joinActLocation = joinActLocation;
        this.joinActGongshi = joinActGongshi;
        this.joinActDeadline = joinActDeadline;
        this.joinActDetials = joinActDetials;
        this.joinActDuty = joinActDuty;
    }

    public String getJoinActName() {
        return joinActName;
    }

    public void setJoinActName(String joinActName) {
        this.joinActName = joinActName;
    }

    public String getJoinActTime() {
        return joinActTime;
    }

    public void setJoinActTime(String joinActTime) {
        this.joinActTime = joinActTime;
    }

    public String getJoinNumOfPlan() {
        return joinNumOfPlan;
    }

    public void setJoinNumOfPlan(String joinNumOfPlan) {
        this.joinNumOfPlan = joinNumOfPlan;
    }

    public String getJoinNumOfJoin() {
        return joinNumOfJoin;
    }

    public void setJoinNumOfJoin(String joinNumOfJoin) {
        this.joinNumOfJoin = joinNumOfJoin;
    }

    public String getJoinActNum() {
        return joinActNum;
    }

    public void setJoinActNum(String joinActNum) {
        this.joinActNum = joinActNum;
    }

    public String getJoinActType() {
        return joinActType;
    }

    public void setJoinActType(String joinActType) {
        this.joinActType = joinActType;
    }

    public String getJoinActLocation() {
        return joinActLocation;
    }

    public void setJoinActLocation(String joinActLocation) {
        this.joinActLocation = joinActLocation;
    }

    public String getJoinActGongshi() {
        return joinActGongshi;
    }

    public void setJoinActGongshi(String joinActGongshi) {
        this.joinActGongshi = joinActGongshi;
    }

    public String getJoinActDeadline() {
        return joinActDeadline;
    }

    public void setJoinActDeadline(String joinActDeadline) {
        this.joinActDeadline = joinActDeadline;
    }

    public String getJoinActDetials() {
        return joinActDetials;
    }

    public void setJoinActDetials(String joinActDetials) {
        this.joinActDetials = joinActDetials;
    }

    public String getJoinActDuty() {
        return joinActDuty;
    }

    public void setJoinActDuty(String joinActDuty) {
        this.joinActDuty = joinActDuty;
    }
}
