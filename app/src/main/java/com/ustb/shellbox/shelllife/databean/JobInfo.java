package com.ustb.shellbox.shelllife.databean;

/**
 * Created by 37266 on 2017/3/21.
 */

public class JobInfo {
    private String content;
    private String href;
    private String time;

    public JobInfo(String content, String href, String time) {
        this.content = content;
        this.href = href;
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
