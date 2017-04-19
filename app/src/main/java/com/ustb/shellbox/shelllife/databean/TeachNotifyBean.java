package com.ustb.shellbox.shelllife.databean;

/**
 * Created by 37266 on 2017/3/7.
 */
public class TeachNotifyBean {
    private String content;
    private String data;
    private String href;

    public TeachNotifyBean(String content, String data, String href) {
        this.content = content;
        this.data = data;
        this.href = href;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
