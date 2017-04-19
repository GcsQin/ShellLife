package com.ustb.shellbox.shelllife.databean;

/**
 * Created by 37266 on 2017/3/2.
 */
public class Constants {
    //URL
    public static final String URL_LOGIN_SERVER="http://elearning.ustb.edu.cn/choose_courses";//
    public static final String URL_ValidateACCOUNT="/j_spring_security_check";//登录
    public static final String URL_InnovationCredit = "http://elearning.ustb.edu.cn/choose_courses/information/singleStuInfo_singleStuInfo_loadSingleStuCxxfPage.action";//创新学分
    public static final String URL_CourseScore = "http://elearning.ustb.edu.cn/choose_courses/information/singleStuInfo_singleStuInfo_loadSingleStuScorePage.action";//课程成绩
    public static final String URL_TeachPlan = "http://elearning.ustb.edu.cn/choose_courses/information/singleStuInfo_singleStuInfo_loadSingleStuTeachProgramPage.action";//教学计划
    public static final String URL_Cet = "information/singleStuInfo_singleStuInfo_loadSingleStuCetPage.action";//小语种;
    public static final String URL_Syllabus = "/choosecourse/commonChooseCourse_courseList_loadTermCourses.action";
    public static final String URL_CourseLog = "/choosecourse/commonChooseCourse_courseList_loadSkxsbLogs.action";
    public static final String URL_ExamInfo = "/choosecourse/commonChooseCourse_examList_loadExamListPage.action";
    public static final String URL_WEB_SELF_SERVICE="http://202.204.60.7:8080/LoginAction.action";
    public static final String URL_TEACH_USTB="http://teach.ustb.edu.cn/";
    public static final String URL_CAMPUS_WEB="http://202.204.48.66/";
    public static final String URL_LIB_SEARCH_START="http://lib.ustb.edu.cn:8080/opac/openlink.php?strSearchType=title&match_flag=forward&historyCount=1&strText=";
    public static final String URL_LIB_SEARCH_END="&doctype=ALL&displaypg=20&showmode=list&sort=CATA_DATE&orderby=desc&dept=ALL";
    public static final String URL_ENTER_LIBRARY_CODE="http://lib.ustb.edu.cn:8080/reader/captcha.php?code=";
    public static final String URL_ENTER_LIBRARY="http://lib.ustb.edu.cn:8080/reader/redr_verify.php";
    public static final String URL_GET_LIBRARY_HISTORY="http://lib.ustb.edu.cn:8080/reader/book_hist.php";
    public static final String URL_GET_LIB_DEBT="http://lib.ustb.edu.cn:8080/reader/fine_pec.php";

    public static final String URL_LOGIN_EPORTAL="http://e.ustb.edu.cn/userPasswordValidate.portal";
    public static final String URL_SEARCH_EPORTAL_INFO="http://e.ustb.edu.cn/index.portal";
    public static final String URL_SEARCH_CARD_INFO_THISMONTH="?.p=Znxjb20ud2lzY29tLnBvcnRhbC5zaXRlLmltcGwuRnJhZ21lbnRXaW5kb3d8Zjg0NDF8dmlld3xub3JtYWx8YWN0aW9uPXhmamw_&flag=";

    public static final String URL_LOGIN_ZHIYUAN="http://zhiyuan.ustb.edu.cn/app.VPClient/index.jsp?m=vpclient&c=user&a=login";
    public static final String URL_ZHIYUAN_INFO="http://zhiyuan.ustb.edu.cn/app.VPClient/index.jsp?m=vpclient&c=index&a=showIndex";
    public static final String URL_HAVE_JOIN_ACT="http://zhiyuan.ustb.edu.cn/app.VPClient/index.jsp?m=vpclient&c=ActivityApply&a=attendActivity";
    public static final String URL_JOIN_ACT="http://zhiyuan.ustb.edu.cn/app.VPClient/index.jsp?m=vpclient&c=ActivityApply&a=recruitingActivity";
    public static final String URL_LOAD_MORE_ACT="&activityName=&activityAddr=&zyznlyq=&activityKeywords=&stage=0&type=0&org=0&state=1&_=";
    public static final String URL_JOIN_AN_ACT="http://zhiyuan.ustb.edu.cn/app.VPClient/index.jsp?m=vpclient&c=ActivityApply&a=joinActivity&activityId=";
    public static final String URL_JOB="http://job.ustb.edu.cn/";

    //handlerConstants
    public static final int HANDLER_UPDATA_VIEWPAGER=1;
    public static final int HANDLER_IMAGE_DOWNLOAD_FINISH=2;
    public static final int HANDLER_ENTER_NEW_EDU_SYS=3;
    public static final int HANDLER_ENTER_NEW_EDU_SYS_FAILURE=4;
    public static final int HANDLER_UPDATA_LISTVIEW=5;
    //edusys
    public static final int HANDLER_ENTER_EDUSYS_FAIL=0x1401;
    public static final int HANDLER_ENTER_EDUSYS_SUCCESSFUL=0x1403;
    public static final int HANDLER_GET_INFO_EDUSYS_FAIL=0x1402;
    //campusweb
    public static final int HANDLER_ENTER_CAMPUS_WEB_FAIL=0x1201;
    public static final int HANDLER_GET_INFO_CAMPUS_WEB_FAIL=0x1202;
    public static final int HANDLER_ENTER_CAMPUSWEB_SUCCESSFUL=0x1203;
    public static final int HANDLER_GET_INFO_CAMPUS_FAIL_BYERROR=0x1204;
    //library
    public static final int HANDLER_GET_VRIFICATION_CODE_LIB=0x1301;
    public static final int HANDLER_LOGIN_FAILURE_LIB=0x1302;
    public static final int HANDLER_LOGIN_SUCCESSFUL_LIB=0x1303;
    public static final int HANDLER_SEARCH_RESULT_ISNULL=0x1304;
    public static final int HANDELER_GET_HISTORY_LIBRARY_FAILER=0X1305;
    public static final int HANDELER_REFRESH_LIB_DETIALS=0x1306;
    public static final int HANDELER_LOADMORE_LIB_DETIAILS=0x1307;
    public static final int HANDELER_UPDATE_DATA_PTR=0x1308;
    public static final int HANDELER_FUNCTION_TO_LOAD=0x1309;
    //eportal
    public static final int HANDELER_ACCESS_EPORTAL_FAIL=0x2101;
    public static final int HANDELER_ACCESS_EPORTAL_SUCCESS=0x2102;
    public static final int HANDELER_GET_EPORTAL_INFO_FAIL=0x2103;
    public static final int HANDELER_SHOW_EPORTAL_INFO=0x2104;
    public static final int HANDELER_SHOW_THIS_MONTH_DETIALS=0x2105;
    //zhiyuan
    public static final int HANDELER_ACCESS_ZHIYUAN_FAIL=0x2201;
    public static final int HANDELER_ACCESS_ZHIYUAN_FAIL_ACCOUNT_WRONG=0x2202;
    public static final int HANDELER_ENTER_ZHIYUAN_CENTER=0x2203;
    public static final int HANDELER_GET_ZHIYUAN_DETIALS_SUCCESS=0x2204;
    public static final int HANDELER_GET_DETIALS_FINISH=0x2205;
    public static final int HANDELER_SHOW_JOIN_DIALOG=0x2206;
    public static final int HANDELER_FUNCTION_TO_LOAD_DETIALS=0x2207;
    //all all以99开头
    public static final int HANDELER_ACCESS_FAILURE=0x9901;//访问网站失败（OnFailure），都会弹出一个Toast打印e
    public static final int HANDELER_SHOW_TOAST=0x9902;//弹出Toast
    public static final int HANDELER_LOADMORE_DETIALS=0x9903;//PullToRefresh加载更多
    public static final int HANDELER_REFRESH_DETIASL=0x9904;
}
