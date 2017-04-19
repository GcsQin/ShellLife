package com.ustb.shellbox.shelllife.databean;

/**
 * Created by 37266 on 2017/3/4.
 */
public class EduScoreInfoBean {
    private String scoreTerm;
    private String scoreCourseID;
    private String scoreCourseName;
    private String scoreCourseType;
    private String scoreStudyTime;
    private String scoreScore;
    private String scoreFirstScore;
    private String scoerFinalScore;
    private String scoreRebuildSign;

    public EduScoreInfoBean(String scoreTerm, String scoreCourseID, String scoreCourseName, String scoreCourseType, String scoreStudyTime, String scoreScore, String scoreFirstScore, String scoerFinalScore, String scoreRebuildSign) {
        this.scoreTerm = scoreTerm;
        this.scoreCourseID = scoreCourseID;
        this.scoreCourseName = scoreCourseName;
        this.scoreCourseType = scoreCourseType;
        this.scoreStudyTime = scoreStudyTime;
        this.scoreScore = scoreScore;
        this.scoreFirstScore = scoreFirstScore;
        this.scoerFinalScore = scoerFinalScore;
        this.scoreRebuildSign = scoreRebuildSign;
    }

    public String getScoreTerm() {
        return scoreTerm;
    }

    public void setScoreTerm(String scoreTerm) {
        this.scoreTerm = scoreTerm;
    }

    public String getScoreCourseID() {
        return scoreCourseID;
    }

    public void setScoreCourseID(String scoreCourseID) {
        this.scoreCourseID = scoreCourseID;
    }

    public String getScoreCourseName() {
        return scoreCourseName;
    }

    public void setScoreCourseName(String scoreCourseName) {
        this.scoreCourseName = scoreCourseName;
    }

    public String getScoreCourseType() {
        return scoreCourseType;
    }

    public void setScoreCourseType(String scoreCourseType) {
        this.scoreCourseType = scoreCourseType;
    }

    public String getScoreStudyTime() {
        return scoreStudyTime;
    }

    public void setScoreStudyTime(String scoreStudyTime) {
        this.scoreStudyTime = scoreStudyTime;
    }

    public String getScoreScore() {
        return scoreScore;
    }

    public void setScoreScore(String scoreScore) {
        this.scoreScore = scoreScore;
    }

    public String getScoreFirstScore() {
        return scoreFirstScore;
    }

    public void setScoreFirstScore(String scoreFirstScore) {
        this.scoreFirstScore = scoreFirstScore;
    }

    public String getScoerFinalScore() {
        return scoerFinalScore;
    }

    public void setScoerFinalScore(String scoerFinalScore) {
        this.scoerFinalScore = scoerFinalScore;
    }

    public String getScoreRebuildSign() {
        return scoreRebuildSign;
    }

    public void setScoreRebuildSign(String scoreRebuildSign) {
        this.scoreRebuildSign = scoreRebuildSign;
    }
}
