package com.ustb.shellbox.shelllife.databean;

/**
 * Created by 37266 on 2017/3/4.
 */
public class EduCxxfInfoBean {
    private String cxxfTem;
    private String stuCxxfType;
    private String cxxfProjectName;
    private String scoreCxxf;
    private String cxxfSignInTime;

    public EduCxxfInfoBean(String cxxfTem, String stuCxxfType, String cxxfProjectName, String scoreCxxf, String cxxfSignInTime) {
        this.cxxfTem = cxxfTem;
        this.stuCxxfType = stuCxxfType;
        this.cxxfProjectName = cxxfProjectName;
        this.scoreCxxf = scoreCxxf;
        this.cxxfSignInTime = cxxfSignInTime;
    }

    public String getCxxfTem() {
        return cxxfTem;
    }

    public void setCxxfTem(String cxxfTem) {
        this.cxxfTem = cxxfTem;
    }

    public String getCxxfProjectName() {
        return cxxfProjectName;
    }

    public void setCxxfProjectName(String cxxfProjectName) {
        this.cxxfProjectName = cxxfProjectName;
    }

    public String getCxxfSignInTime() {
        return cxxfSignInTime;
    }

    public void setCxxfSignInTime(String cxxfSignInTime) {
        this.cxxfSignInTime = cxxfSignInTime;
    }

    public String getScoreCxxf() {
        return scoreCxxf;
    }

    public void setScoreCxxf(String scoreCxxf) {
        this.scoreCxxf = scoreCxxf;
    }

    public String getStuCxxfType() {
        return stuCxxfType;
    }

    public void setStuCxxfType(String stuCxxfType) {
        this.stuCxxfType = stuCxxfType;
    }
}
